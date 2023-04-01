package com.seabreeze.robot.data.websocket

/**
 * User: milan
 * Time: 2022/5/28 14:28
 * Des:
 */
class WsStatus {
    sealed class Connect {
        class Connected(val time: Long) : Connect()//已连接
        object DisConnected : Connect()//未连接
        class Connecting(val type: Int) : Connect()//连接中
    }

    internal object Connecting {
        const val CONNECTING = 0
        const val RECONNECT = 1
        const val RECONNECTING = 2
    }

    internal object CODE {
        const val NORMAL_CLOSE = 1000
        const val ABNORMAL_CLOSE = 1001
    }

    internal object TIP {
        const val NORMAL_CLOSE = "normal close"
        const val ABNORMAL_CLOSE = "abnormal close"
    }
}