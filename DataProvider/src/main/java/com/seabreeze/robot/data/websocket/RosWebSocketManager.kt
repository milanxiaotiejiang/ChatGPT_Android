package com.seabreeze.robot.data.websocket

import com.seabreeze.robot.data.netty.NettyImpl
import com.seabreeze.robot.data.netty.sendJsonMsg
import com.seabreeze.robot.data.ok.OkhttpWebSocketImpl
import com.seabreeze.robot.data.ok.sendJsonMsg
import com.seabreeze.robot.data.websocket.model.hex.base.PDU
import com.seabreeze.robot.data.websocket.model.json.base.RosRequest
import com.seabreeze.robot.data.websocket.request.callback.JsonRequestCallback
import java.util.concurrent.ConcurrentLinkedDeque

/**
 * User: milan
 * Time: 2021/9/3 11:50
 * Des:
 */
open class RosWebSocketManager private constructor() : AbstractWebSocket() {

    var isAppTest = false
    var isNetty = true
    var isAuth = false

    var mWebSocket: AbstractWebSocket = if (isNetty)
        NettyImpl()
    else
        OkhttpWebSocketImpl()


    override fun connectWebSocket(host: String, port: Int, needReconnect: Boolean) {
        mWebSocket.connectWebSocket(host, port, needReconnect)
    }

    override fun disconnect() {
        mWebSocket.disconnect()
    }

    override fun isConnected() = mWebSocket.isConnected()

    override fun sendSubscribeMsg(topic: String) {
        if (isConnected())
            mWebSocket.sendSubscribeMsg(topic)
    }

    override fun sendMoveStop(currentProgress: Int) {
        mWebSocket.sendMoveStop(currentProgress)
    }

    override fun sendOneWayMsg(pdu: PDU) {
        mWebSocket.sendOneWayMsg(pdu)
    }

    private val onConnectListeners = ConcurrentLinkedDeque<IWebSocketManager.OnConnectListener>()

    fun registerConnectListener(onConnectListener: IWebSocketManager.OnConnectListener) {
        if (!onConnectListeners.contains(onConnectListener)) {
            onConnectListeners.add(onConnectListener)
        }
    }

    fun unRegisterConnectListener(onConnectListener: IWebSocketManager.OnConnectListener) {
        onConnectListeners.remove(onConnectListener)
    }

    fun listConnectListeners() = onConnectListeners

    companion object {
        val INSTANCE: RosWebSocketManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            RosWebSocketManager()
        }

        const val REQUEST_MAX_ATTEMPTS = 1
        const val DEFAULT_DELAY_FOR_REQUEST = 6000

        const val DEFAULT_DELAY_FOR_REQUEST_LONG = DEFAULT_DELAY_FOR_REQUEST * 10//1分钟
        const val DEFAULT_DELAY_FOR_REQUEST_MAX = DEFAULT_DELAY_FOR_REQUEST * 30//3分钟
        const val DEFAULT_DELAY_FOR_REQUEST_EXTREMELY = DEFAULT_DELAY_FOR_REQUEST * 100//10分钟

        const val DEFAULT_WEB_SOCKET_PORT = 9090
    }
}

inline fun <T, reified R> RosWebSocketManager.sendJsonMsg(
    rosRequest: RosRequest<T>,
    overtime: Long = RosWebSocketManager.DEFAULT_DELAY_FOR_REQUEST.toLong(),
    callback: JsonRequestCallback<R>
) {
    if (mWebSocket is OkhttpWebSocketImpl) {
        val webSocket = mWebSocket as OkhttpWebSocketImpl
        webSocket.sendJsonMsg(rosRequest, overtime, callback)
    } else if (mWebSocket is NettyImpl) {
        val webSocket = mWebSocket as NettyImpl
        webSocket.sendJsonMsg(rosRequest, overtime, callback)
    }

}