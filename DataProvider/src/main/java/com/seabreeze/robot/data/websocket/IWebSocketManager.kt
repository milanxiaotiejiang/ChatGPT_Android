package com.seabreeze.robot.data.websocket

import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.BaseThrowable.ExternalThrowable

/**
 * User: milan
 * Time: 2021/9/3 11:40
 * Des:
 */
interface IWebSocketManager {
    fun startConnect(host: String, port: Int)
    fun stopConnect()
    fun isConnected(): Boolean
    fun sendMessage(msg: String): BaseThrowable.InsideThrowable?

    /**
     * 连接监听
     */
    interface OnConnectListener {
        fun onSuccess(host: String)
        fun onFailed(throwable: BaseThrowable)
        fun onClosed(code: Int, reason: String) {}
        fun onClosing(code: Int, reason: String) {}
        fun onReconnect() {}
    }

    /**
     * 通道消息处理（接收消息）
     */
    interface OnReceivedMessageListener {
        fun onMessageReceived(topic: String, op: String, msg: String);
        fun onMessageReceivedError(throwable: ExternalThrowable)
    }

    /**
     * 发送消息监听
     */
    interface OnSendMessageListener {
        fun onSendMessage(msg: String, success: Boolean)
        fun onException(throwable: ExternalThrowable)
    }
}