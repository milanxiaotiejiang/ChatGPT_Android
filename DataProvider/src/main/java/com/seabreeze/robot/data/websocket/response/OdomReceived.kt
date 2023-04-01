package com.seabreeze.robot.data.websocket.response

import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextOdom
import com.seabreeze.robot.data.websocket.model.json.Odom

/**
 * User: milan
 * Time: 2021/11/25 13:52
 * Des:
 */
class OdomReceived(val msg: String) : MessageReceivedStrategy {
    override fun dataProcessing() {
        msg.gToBean<Odom>()?.nextOdom()
    }
}