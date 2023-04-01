package com.seabreeze.robot.data.netty

import android.os.Handler
import android.os.Looper
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.DispatcherExecutor
import com.seabreeze.robot.data.R
import com.seabreeze.robot.data.websocket.AbstractWebSocket
import com.seabreeze.robot.data.websocket.IWebSocketManager
import com.seabreeze.robot.data.websocket.RosWebSocketManager
import com.seabreeze.robot.data.websocket.WsStatus
import com.seabreeze.robot.data.websocket.model.hex.action.ActionMove
import com.seabreeze.robot.data.websocket.model.hex.base.PDU
import com.seabreeze.robot.data.websocket.model.json.base.RosRequest
import com.seabreeze.robot.data.websocket.request.callback.JsonRequestCallback


/**
 * User: milan
 * Time: 2022/2/11 16:26
 * Des:
 */
class NettyImpl : AbstractWebSocket() {

    private val mMainHandler: Handler = Handler(Looper.getMainLooper())

    private val communicationListener = object : CommunicationListener {}

    private val mCommunicationCompleteListener = object : CommunicationCompleteListener {
        override fun onCommunicationComplete(host: String) {
            mMainHandler.connectCallback {
                it.onSuccess(host)
            }
        }
    }

    private val mCommunicationErrorListener = object : CommunicationErrorListener {
        override fun onCommunicationFailed(throwable: BaseThrowable) {
            throwable.printStackTrace()
            mMainHandler.connectCallback {
                it.onFailed(throwable)
            }
        }
    }

    var mNettyThread: NettyThread? = null

    private var mHost: String = ""

    override fun connectWebSocket(host: String, port: Int, needReconnect: Boolean) {
        mHost = host
        val remoteAddress = RemoteAddress(host, port)

        when (ConnectStatus.getCurrentStatus()) {
            is WsStatus.Connect.Connecting -> {
                val connecting = ConnectStatus.getCurrentStatus() as WsStatus.Connect.Connecting
                if (connecting.type == WsStatus.Connecting.RECONNECT) {
                    ConnectStatus.setReconnecting()
                    realConnectAddress(remoteAddress, needReconnect)
                }
            }
            is WsStatus.Connect.Connected -> {
                val connected = ConnectStatus.getCurrentStatus() as WsStatus.Connect.Connected
                ConnectStatus.setConnected(connected.time)
                mNettyThread?.onConnectComplete()
            }
            is WsStatus.Connect.DisConnected -> {
                ConnectStatus.setConnecting()
                realConnectAddress(remoteAddress, needReconnect)
            }
        }
    }

    private fun realConnectAddress(remoteAddress: RemoteAddress, needReconnect: Boolean) {
        disconnect()
        mNettyThread = NettyThread(remoteAddress, needReconnect, communicationListener)
            .apply {
                mCompleteListener = mCommunicationCompleteListener
                mErrorListener = mCommunicationErrorListener
            }
        DispatcherExecutor.iOExecutor.execute(mNettyThread)
    }

    override fun disconnect() {
        mNettyThread?.disconnect()
        mNettyThread = null
    }

    override fun isConnected() = mNettyThread?.isConnected() ?: false

    override fun sendSubscribeMsg(topic: String) {
        if (isConnected()) {
            mNettyThread?.sendSubscribeMsg(topic)
        }
    }

    override fun sendMoveStop(currentProgress: Int) {
        if (isConnected()) {
            return
        }
        val speed = currentProgress - 10
        val actionMove = ActionMove(ActionMove.DirectionType.STOP, speed, speed)
        sendOneWayMsg(actionMove)
        if (currentProgress < 0) {
            return
        }
        mMainHandler.postDelayed({
            sendMoveStop(speed)
        }, 100)
    }

    override fun sendOneWayMsg(pdu: PDU) {
        if (isConnected()) {
            mNettyThread?.sendOneWayMsg(pdu)
        }
    }

    companion object {
        const val TEST_NAME = "admin"
        const val TEST_PASS = "1234567890"
    }
}

inline fun <T, reified R> NettyImpl.sendJsonMsg(
    rosRequest: RosRequest<T>,
    overtime: Long = RosWebSocketManager.DEFAULT_DELAY_FOR_REQUEST.toLong(),
    callback: JsonRequestCallback<R>
) {
    if (isConnected()) {
        mNettyThread?.sendJsonMsg(rosRequest, overtime, callback)
    }
}

private fun Handler.connectCallback(method: (l: IWebSocketManager.OnConnectListener) -> Unit) {
    post {
        RosWebSocketManager.INSTANCE.listConnectListeners()
            .forEach { listener ->
                method(listener)
            }
    }
}