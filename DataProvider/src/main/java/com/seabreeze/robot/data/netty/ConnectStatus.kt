package com.seabreeze.robot.data.netty

import com.elvishew.xlog.XLog
import com.seabreeze.robot.data.websocket.WsStatus

/**
 * User: milan
 * Time: 2022/6/18 18:03
 * Des:
 */
object ConnectStatus {
    @Volatile
    private var mCurrentStatus: WsStatus.Connect = WsStatus.Connect.DisConnected //webSocket连接状态

    @Synchronized
    private fun setCurrentStatus(currentStatus: WsStatus.Connect) {
        mCurrentStatus = currentStatus
        onCommunicationStatus(mCurrentStatus)
    }

    @Synchronized
    fun setConnected(time: Long = System.currentTimeMillis()) {
        setCurrentStatus(WsStatus.Connect.Connected(time))
    }

    @Synchronized
    fun setDisConnected() {
        setCurrentStatus(WsStatus.Connect.DisConnected)
    }

    @Synchronized
    fun setConnecting() {
        setCurrentStatus(WsStatus.Connect.Connecting(WsStatus.Connecting.CONNECTING))
    }

    @Synchronized
    fun setReconnect() {
        setCurrentStatus(WsStatus.Connect.Connecting(WsStatus.Connecting.RECONNECT))
    }

    @Synchronized
    fun setReconnecting() {
        setCurrentStatus(WsStatus.Connect.Connecting(WsStatus.Connecting.RECONNECTING))
    }

    fun getCurrentStatus() = mCurrentStatus

    fun getConnectingStatus(): Int {
        if (mCurrentStatus is WsStatus.Connect.Connecting) {
            return (mCurrentStatus as WsStatus.Connect.Connecting).type
        }
        return -1
    }

    private fun onCommunicationStatus(wsStatus: WsStatus.Connect) {
        when (wsStatus) {
            is WsStatus.Connect.Connected -> {
                XLog.d("onCommunicationStatus 已连接")
            }
            is WsStatus.Connect.Connecting -> {
                when (wsStatus.type) {
                    WsStatus.Connecting.CONNECTING -> {
                        XLog.d("onCommunicationStatus 连接中")
                    }
                    WsStatus.Connecting.RECONNECT -> {
                        XLog.d("onCommunicationStatus 准备重试")
                    }
                    WsStatus.Connecting.RECONNECTING -> {
                        XLog.d("onCommunicationStatus 重试中")
                    }
                }
            }
            is WsStatus.Connect.DisConnected -> {
                XLog.d("onCommunicationStatus 未连接或连接失败")
            }
        }
    }
}