package com.seabreeze.robot.data.websocket.response

/**
 * User: milan
 * Time: 2021/11/25 13:44
 * Des:
 */
class MessageReceived(private val strategy: MessageReceivedStrategy) {

    fun startDataProcessing() {
        strategy.dataProcessing()
    }
}

interface MessageReceivedStrategy {
    fun dataProcessing()
}