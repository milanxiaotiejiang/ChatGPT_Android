package com.seabreeze.robot.data.websocket.response

import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextMap
import com.seabreeze.robot.data.websocket.model.json.RosMapData

/**
 * User: milan
 * Time: 2021/11/25 13:49
 * Des:
 */
class MapReceived(val msg: String) : MessageReceivedStrategy {
    override fun dataProcessing() {
        val gToBean = msg.gToBean<RosMapData>()
        gToBean?.apply {
            info
            val width = info.width
            val height = info.height
            if (width > 0 && height > 0) {
                nextMap()
            }
        }
    }
}