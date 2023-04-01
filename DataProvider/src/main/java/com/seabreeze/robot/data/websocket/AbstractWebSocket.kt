package com.seabreeze.robot.data.websocket

import com.seabreeze.robot.data.websocket.model.hex.base.PDU

/**
 * User: milan
 * Time: 2022/2/11 15:51
 * Des:
 */
abstract class AbstractWebSocket {
    abstract fun connectWebSocket(
        host: String,
        port: Int = RosWebSocketManager.DEFAULT_WEB_SOCKET_PORT,
        needReconnect: Boolean = true
    )

    abstract fun disconnect()

    abstract fun isConnected(): Boolean

    abstract fun sendSubscribeMsg(topic: String)

    abstract fun sendMoveStop(currentProgress: Int)

    abstract fun sendOneWayMsg(pdu: PDU)

    private fun fibonacci(number: Long): Long {
        return if (number == 0L || number == 1L)
            number
        else fibonacci(number - 1) + fibonacci(number - 2)
    }

}