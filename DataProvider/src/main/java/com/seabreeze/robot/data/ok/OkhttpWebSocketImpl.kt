package com.seabreeze.robot.data.ok

import android.os.Handler
import android.os.Looper
import com.elvishew.xlog.XLog
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.utils.HexUtil
import com.seabreeze.robot.data.DataApplication
import com.seabreeze.robot.data.netty.CommunicationThread
import com.seabreeze.robot.data.netty.StreamGenerator
import com.seabreeze.robot.data.websocket.*
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextError
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextReceived
import com.seabreeze.robot.data.websocket.model.hex.action.ActionMove
import com.seabreeze.robot.data.websocket.model.hex.base.PDU
import com.seabreeze.robot.data.websocket.model.json.base.RosRequest
import com.seabreeze.robot.data.websocket.request.*
import com.seabreeze.robot.data.websocket.request.callback.JsonRequestCallback
import com.seabreeze.robot.data.websocket.response.*
import org.json.JSONObject
import java.lang.ref.WeakReference
import java.util.*
import java.util.concurrent.LinkedBlockingQueue

/**
 * User: milan
 * Time: 2022/2/11 15:51
 * Des:
 */
class OkhttpWebSocketImpl : AbstractWebSocket() {

    private var mCommunicationThread: CommunicationThread? = null

    private val mClient: WebSocketClient by lazy {
        val webSocketClient = WebSocketClient(DataApplication.okHttpClient)
        webSocketClient.setOnConnectListener(object : IWebSocketManager.OnConnectListener {
            override fun onSuccess(host: String) {
                RosWebSocketManager.INSTANCE.listConnectListeners().forEach {
                    it.onSuccess(host)
                }
            }

            override fun onFailed(throwable: BaseThrowable) {
                RosWebSocketManager.INSTANCE.listConnectListeners().forEach {
                    it.onFailed(throwable)
                }
            }
        })

        webSocketClient.setOnReceivedMessageListener(
            object : IWebSocketManager.OnReceivedMessageListener {
                override fun onMessageReceived(topic: String, op: String, msg: String) {
                    var messageReceivedStrategy: MessageReceivedStrategy? = null
                    when (op) {
                        Request.RequestOp.PUBLISH -> {
                            when (topic) {
                                RequestTopicManager.map() -> {
//                                    val response = msg.gToBean<Response>()
//                                    response?.apply {
//                                        messageReceivedStrategy = MapReceived(data)
//                                    }
                                    messageReceivedStrategy = MapReceived(msg)
                                }
                                RequestTopicManager.odom() -> {
                                    messageReceivedStrategy = OdomReceived(msg)
                                }
                                RequestTopicManager.robotStatus() -> {
                                        messageReceivedStrategy = RobotStatusReceived(op, msg)
                                }
                                RequestTopicManager.errorApp() -> {
                                        messageReceivedStrategy = ErrorReceived(msg)
                                }
                                RequestTopicManager.noticeApp() -> {
                                        messageReceivedStrategy = NoticeReceived(msg)
                                }
                                RequestTopicManager.taskPoint() -> {
                                        messageReceivedStrategy = TaskPointReceived(msg)
                                }
                                RequestTopicManager.scanLaser() -> {
                                    messageReceivedStrategy = ScanLaserReceived(msg)
                                }
                                RequestTopicManager.checkApp() -> {
                                        messageReceivedStrategy = CheckAppReceived(msg)
                                }
                                RequestTopicManager.pathPlanningNode() -> {
                                    messageReceivedStrategy = GlobalReceived(msg)
                                }
                                RequestTopicManager.materialStatus() -> {
                                        messageReceivedStrategy = MaterialStatusReceived(op, msg)
                                }
                                RequestTopicManager.response() -> {
                                    XLog.e("response topic : $topic , op : $op , msg : $msg")
                                        if (msg.length == 2) {
                                            val byteArray = HexUtil.decodeHex(msg)
                                            if (byteArray.size == 1) {
                                                val byte = byteArray[0]
                                                val flag = byte.toInt()
                                                val request = onReceiveCallback(flag)
                                                request?.resultSuccess()
                                                processNextRequest()
                                            }
                                        }
                                }
                                RequestTopicManager.responseJSON() -> {
                                    XLog.w("responseJSON topic : $topic , op : $op , msg : $msg")
                                    if (mTimeOutRequestRunnable != null) {
                                        val request = mTimeOutRequestRunnable!!.request
                                        if (request is JsonRequest<*, *>) {
                                            XLog.w("afferentMsg $msg")
                                            request.analysis(
                                                requestRecord = request,
                                                afferentMsg = msg,
                                                front = { cancelTimeOutRequestRunnable() },
                                                after = { processNextRequest() })
                                        }
                                    }
                                }
                                else -> {
                                    XLog.e("else topic : $topic , op : $op , msg : $msg")
                                }
                            }
                        }
                        Request.RequestOp.SUBSCRIBE -> {
                            XLog.e("topic : $topic , op : $op , msg : $msg")
                        }
                        Request.RequestOp.AUTH -> {
                            XLog.e("topic : $topic , op : $op , msg : $msg")
                        }
                        else -> msg.nextReceived()
                    }

                    messageReceivedStrategy?.apply {
                        val messageReceived = MessageReceived(this)
                        messageReceived.startDataProcessing()
                    }
                }

                override fun onMessageReceivedError(throwable: BaseThrowable.ExternalThrowable) {
                    throwable.nextError()
                }

            })
        webSocketClient
    }

    private var mTimeOutRequestRunnable: TimeOutRequestRunnable? = null

    private val mRequestsQueue: Queue<Request> = LinkedBlockingQueue()

    @Volatile
    private var isQueueProcessing = false

    private val mHandler = Handler(Looper.getMainLooper())

    private var mHost: String = ""

    override fun connectWebSocket(host: String, port: Int, needReconnect: Boolean) {
        mHost = host
        mClient.startConnect(host, port)
    }

    override fun disconnect() {
        mClient.stopConnect()
        resetQueue()
    }

    override fun isConnected() = mClient.isConnected()

    override fun sendSubscribeMsg(topic: String) {
        val subscribeRequest = SubscribeRequest(topic)
        val requestMessage = subscribeRequest.getRequest()
        if (isConnected()) mClient.sendMessage(requestMessage)
    }

    override fun sendMoveStop(currentProgress: Int) {
        val speed = currentProgress - 10
        val actionMove = ActionMove(ActionMove.DirectionType.STOP, speed, speed)
        sendOneWayMsg(actionMove)
        if (currentProgress < 0) {
            return
        }
        mHandler.postDelayed({
            sendMoveStop(speed)
        }, 100)
    }

    override fun sendOneWayMsg(pdu: PDU) {
        val oneWayRequest = OneWayRequest(
            pdu.pduMessage(StreamGenerator.getStreamId()),
        )
        val requestMessage = oneWayRequest.getRequest()
        if (isConnected()) mClient.sendMessage(requestMessage)
    }

    private fun resetQueue() {
        mRequestsQueue.clear()
        isQueueProcessing = false
        cancelTimeOutRequestRunnable()
    }

    fun addToRequestsQueue(request: Request) {
        if (request.attempts < RosWebSocketManager.REQUEST_MAX_ATTEMPTS) {
            mRequestsQueue.add(request)
        }

        if (!isQueueProcessing) {
            processNextRequest()
        }
    }

    private fun processNextRequest() {
        // 如果请求丢失并且没有回调，则该服务必须能够启动一个新请求。
        isQueueProcessing = true
        // 如果超时运行，我们必须等待它完成
        if (mTimeOutRequestRunnable != null) {
            return
        }
        // 队列为空：无请求处理
        if (mRequestsQueue.size <= 0) {
            isQueueProcessing = false
            return
        }
        // 处理下一个请求
        val request = mRequestsQueue.remove()
        request.increaseAttempts()
        val requestMessage = request.getRequest()

        request.mCallback?.onBefore()
        // 知道请求是否已成功启动
        mTimeOutRequestRunnable = TimeOutRequestRunnable(this, request)
        mHandler.postDelayed(mTimeOutRequestRunnable!!, request.overtime)
        if (RosWebSocketManager.INSTANCE.isAppTest) {
            if (request is JsonRequest<*, *>) {
                mHandler.postDelayed({
                    if (mTimeOutRequestRunnable != null) {
                        request.analysis(
                            requestRecord = mTimeOutRequestRunnable!!.request,
                            afferentMsg = JSONObject(requestMessage).getString("msg"),
                            front = { cancelTimeOutRequestRunnable() },
                            after = { processNextRequest() })
                    }
                }, 300)
            } else {
                cancelTimeOutRequestRunnable()
                request.resultSuccess()
                processNextRequest()
            }

        } else {
            if (isConnected()) {
                val socketThrowable = mClient.sendMessage(requestMessage)
                if (socketThrowable != null) {
                    cancelTimeOutRequestRunnable()
                    onRequestFailed(request, socketThrowable)
                }
            } else {
                onRequestFailed(request, SocketSendInsideThrowable())
            }
        }
    }

    private fun onRequestFailed(request: Request?, insideThrowable: BaseThrowable.InsideThrowable) {
        if (request != null) {
            if (request.attempts < RosWebSocketManager.REQUEST_MAX_ATTEMPTS) {
                addToRequestsQueue(request)
            } else {
                request.apply {
                    resultFail(insideThrowable)
                }
            }
        }
        // 队列未释放，这是失败的预期请求
        processNextRequest()
    }

    private fun cancelTimeOutRequestRunnable() {
        if (mTimeOutRequestRunnable != null) {
            mHandler.removeCallbacks(mTimeOutRequestRunnable!!)
            mTimeOutRequestRunnable = null
        }
    }

    private fun onReceiveCallback(flag: Int): Request? {
        if (mTimeOutRequestRunnable != null) {
            val request = mTimeOutRequestRunnable!!.request
            val msg = request.msg
            val decodeHex = HexUtil.decodeHex(msg)
            val requestFlag = decodeHex[decodeHex.size - 2].toInt()
            if (requestFlag == flag) {
                cancelTimeOutRequestRunnable()
                return request
            }
            return null
        }
        return null
    }

    class TimeOutRequestRunnable(val webSocket: OkhttpWebSocketImpl, val request: Request) :
        Runnable {
        private var mWeakReference: WeakReference<OkhttpWebSocketImpl> = WeakReference(webSocket)

        override fun run() {
            mWeakReference.get()?.apply {
                mTimeOutRequestRunnable = null
                onRequestFailed(request, SocketOvertimeThrowable())
            }
        }
    }
}


inline fun <T, reified R> OkhttpWebSocketImpl.sendJsonMsg(
    rosRequest: RosRequest<T>,
    overtime: Long = RosWebSocketManager.DEFAULT_DELAY_FOR_REQUEST.toLong(),
    callback: JsonRequestCallback<R>
) {
    rosRequest.id = StreamGenerator.getStreamId()

    val request = JsonRequest<T, R>(rosRequest)

    request.mCallback = callback
    request.setTypeToken<R>()
    request.overtime = overtime

    addToRequestsQueue(request)
}