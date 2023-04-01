package com.seabreeze.robot.data.websocket

import android.os.Handler
import android.os.Looper
import com.elvishew.xlog.XLog
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.BaseThrowable.ExternalThrowable
import com.seabreeze.robot.base.ext.tool.isNetworkConnect
import com.seabreeze.robot.data.websocket.IWebSocketManager.OnReceivedMessageListener
import com.seabreeze.robot.data.websocket.IWebSocketManager.OnSendMessageListener
import com.seabreeze.robot.data.websocket.WsStatus.Connecting.CONNECTING
import com.seabreeze.robot.data.websocket.WsStatus.Connecting.RECONNECT
import com.seabreeze.robot.data.websocket.WsStatus.Connecting.RECONNECTING
import okhttp3.*
import okio.ByteString
import org.json.JSONObject
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock
import kotlin.math.min

/**
 * User: milan
 * Time: 2021/9/3 11:39
 * Des:
 */
class WebSocketClient(private var mOkHttpClient: OkHttpClient) : IWebSocketManager {

    private lateinit var wsUrl: String
    private lateinit var webSocket: WebSocket

    @Volatile
    private var mCurrentStatus: WsStatus.Connect = WsStatus.Connect.DisConnected //webSocket连接状态

    private var isManualClose = false //是否为手动关闭webSocket连接

    private val mLock: Lock = ReentrantLock()

    private val mMainHandler = Handler(Looper.getMainLooper())

    @Volatile
    private var reconnectCount = 0 //重连次数

    private val reconnectRunnable = Runnable {
        mOnConnectListener?.run {
            onReconnect()
        }
        buildConnect()
    }

    private var mOnConnectListener: IWebSocketManager.OnConnectListener? = null
    private var mOnReceivedMessageListener: OnReceivedMessageListener? = null
    private val mOnSendMessageListener: OnSendMessageListener? = null

    private val mWebSocketListener: WebSocketListener = object : WebSocketListener() {
        // WebSocket 连接建立
        override fun onOpen(webSocket: WebSocket, response: Response) {

            this@WebSocketClient.webSocket = webSocket

            setCurrentStatus(WsStatus.Connect.Connected(System.currentTimeMillis()))
            connectReset()

            mOnConnectListener?.run {
                if (Looper.getMainLooper() != Looper.myLooper()) {
                    mMainHandler.post { onSuccess(mHost) }
                } else {
                    onSuccess(mHost)
                }
            }
        }

        // 收到服务端发送来的 String 类型消息
        override fun onMessage(webSocket: WebSocket, text: String) {
            mOnReceivedMessageListener?.run {
                try {
                    val jsonObject = JSONObject(text)
                    val topic = jsonObject.getString("topic")
                    val op = jsonObject.getString("op")
                    val msg = jsonObject.getString("msg")
                    onMessageReceived(topic, op, msg)
                } catch (e: Exception) {
                    onMessageReceivedError(ExternalThrowable(e))
                    XLog.e(e)
                }
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            val request = webSocket.request()
            val headers = request.headers
        }

        // 收到服务端发来的 CLOSE 帧消息，准备关闭连接
        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            mMainHandler.removeCallbacks(reconnectRunnable)
            mOkHttpClient.dispatcher.cancelAll()
            mOnConnectListener?.run {
                if (Looper.getMainLooper() != Looper.myLooper()) {
                    mMainHandler.post { onClosing(code, reason) }
                } else {
                    onClosing(code, reason)
                }
            }
        }

        // WebSocket 连接关闭
        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            mMainHandler.removeCallbacks(reconnectRunnable)
            mOkHttpClient.dispatcher.cancelAll()
            mOnConnectListener?.run {
                if (mOnConnectListener != null) {
                    if (Looper.getMainLooper() != Looper.myLooper()) {
                        mMainHandler.post { onClosed(code, reason) }
                    } else {
                        onClosed(code, reason)
                    }
                }
            }
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            XLog.e("WebSocketClient onFailure " + t.message)
            if (mCurrentStatus is WsStatus.Connect.Connecting) {
                val connecting = mCurrentStatus as WsStatus.Connect.Connecting
                if (connecting.type == RECONNECTING) {
                    if (!(!isNeedReconnect or isManualClose)) {
                        if (isNetworkConnect) {
                            setCurrentStatus(WsStatus.Connect.Connecting(RECONNECT))
                            tryReconnect(Throwable("Sending failed, trying to reconnect"))
                        }
                    }
                }
            } else {
                if (!(!isNeedReconnect or isManualClose)) {
                    if (isNetworkConnect) {
                        if (mCurrentStatus is WsStatus.Connect.Connected) {
                            val connected = mCurrentStatus as WsStatus.Connect.Connected
                            if (System.currentTimeMillis() - connected.time > 5000) {
                                setCurrentStatus(WsStatus.Connect.Connecting(RECONNECT))
                                tryReconnect(Throwable("Sending failed, trying to reconnect"))
                            }
                        }
                    }
                }
            }

            mOnConnectListener?.run {
                if (Looper.getMainLooper() != Looper.myLooper()) {
                    mMainHandler.post { onFailed(ExternalThrowable(t)) }
                } else {
                    onFailed(ExternalThrowable(t))
                }
            }
        }
    }

    private var mHost: String = ""

    override fun startConnect(host: String, port: Int) {
        mHost = host
        isManualClose = false
        wsUrl = "ws://$host:$port"
        buildConnect()
    }

    override fun stopConnect() {
        isManualClose = true
        disconnect()
    }

    @Synchronized
    override fun isConnected() = mCurrentStatus is WsStatus.Connect.Connected

    override fun sendMessage(msg: String): BaseThrowable.InsideThrowable? {
        XLog.e("send : $msg")

        if (!this::webSocket.isInitialized) {
            return SocketUnInitThrowable()
        }
        when (mCurrentStatus) {
            is WsStatus.Connect.Connected -> {
                val isSend = webSocket.send(msg)
                //发送消息失败，尝试重连
                if (!isSend) {
                    return SocketSendInsideThrowable()
                }
                return null
            }
            is WsStatus.Connect.Connecting -> {
                val connecting = mCurrentStatus as WsStatus.Connect.Connecting
                return when (connecting.type) {
                    CONNECTING -> {
                        SocketConnectingThrowable()
                    }
                    RECONNECT -> {
                        SocketReconnectThrowable()
                    }
                    RECONNECTING -> {
                        SocketReconnectThrowable()
                    }
                    else -> {
                        SocketUnknownThrowable()
                    }
                }
            }
            is WsStatus.Connect.DisConnected -> {
                return SocketDisconnectedThrowable()
            }
            else -> {
                return SocketUnknownThrowable()
            }
        }
    }

    fun setOnConnectListener(listener: IWebSocketManager.OnConnectListener) {
        mOnConnectListener = listener
    }

    fun setOnReceivedMessageListener(listener: OnReceivedMessageListener) {
        mOnReceivedMessageListener = listener
    }

    @Synchronized
    private fun setCurrentStatus(currentStatus: WsStatus.Connect) {
        mCurrentStatus = currentStatus
    }

    @Synchronized
    private fun buildConnect() {
        if (!isNetworkConnect) {
            setCurrentStatus(WsStatus.Connect.DisConnected)
            return
        }
        when (mCurrentStatus) {
            is WsStatus.Connect.Connecting -> {
                val connecting = mCurrentStatus as WsStatus.Connect.Connecting
                if (connecting.type == RECONNECTING) {
                    realConnect()
                }
            }
            is WsStatus.Connect.Connected -> {
                mOnConnectListener?.run {
                    if (Looper.getMainLooper() != Looper.myLooper()) {
                        mMainHandler.post { onSuccess(mHost) }
                    } else {
                        onSuccess(mHost)
                    }
                }
            }
            is WsStatus.Connect.DisConnected -> {
                setCurrentStatus(WsStatus.Connect.Connecting(CONNECTING))
                realConnect()
            }
        }
    }

    private fun realConnect() {
        mOkHttpClient.dispatcher.cancelAll()
        val request: Request = Request.Builder()
            .url(wsUrl)
            .build()
        try {
            mLock.lockInterruptibly()
            try {
                mOkHttpClient.newWebSocket(request, mWebSocketListener)
            } finally {
                mLock.unlock()
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    private fun disconnect() {
        connectReset()

        if (mCurrentStatus is WsStatus.Connect.DisConnected) {
            return
        }

        if (this::webSocket.isInitialized) {
            val isClosed = webSocket.close(WsStatus.CODE.NORMAL_CLOSE, WsStatus.TIP.NORMAL_CLOSE)

            //非正常关闭连接
            if (!isClosed) {
                mOnConnectListener?.run {
                    if (Looper.getMainLooper() != Looper.myLooper()) {
                        mMainHandler.post {
                            onClosed(WsStatus.CODE.ABNORMAL_CLOSE, WsStatus.TIP.ABNORMAL_CLOSE)
                        }
                    } else {
                        onClosed(WsStatus.CODE.ABNORMAL_CLOSE, WsStatus.TIP.ABNORMAL_CLOSE)
                    }
                }
            }
        }
        setCurrentStatus(WsStatus.Connect.DisConnected)
    }

    private fun connectReset() {
        reconnectCount = 0
        mMainHandler.removeCallbacks(reconnectRunnable)
//        mOkHttpClient.dispatcher.cancelAll()
    }

    /**
     * 只在发送失败后重试
     */
    private fun tryReconnect(t: Throwable) {
        XLog.e(" tryReconnect  " + t.message)
        if (reconnectCount > MAX_CONNECT_COUNT) {
            setCurrentStatus(WsStatus.Connect.DisConnected)
            return
        }
        XLog.e("tryReconnect $reconnectCount")
        setCurrentStatus(WsStatus.Connect.Connecting(RECONNECTING))
        mMainHandler.postDelayed(
            reconnectRunnable,
            min(reconnectCount * RECONNECT_INTERVAL, RECONNECT_MAX_TIME)
        )
        reconnectCount++
    }

    companion object {
        private const val RECONNECT_INTERVAL = 10 * 1000L //重连自增步长
        private const val RECONNECT_MAX_TIME = 30 * 1000L//最大重连间隔
        private const val isNeedReconnect = true //是否需要断线自动重连
        private const val MAX_CONNECT_COUNT = 3
    }
}