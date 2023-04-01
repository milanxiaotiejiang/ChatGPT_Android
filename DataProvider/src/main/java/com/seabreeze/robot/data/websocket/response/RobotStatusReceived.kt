package com.seabreeze.robot.data.websocket.response

import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextAromaDiffuser
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextCurrentExecuteTime
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextElectric
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextIsCharging
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextWater
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextWorkStateInfo
import com.seabreeze.robot.data.websocket.model.json.VersionSubscribe
import com.seabreeze.robot.data.websocket.model.json.WorkStatusSubscribeV1

/**
 * User: milan
 * Time: 2021/11/25 13:54
 * Des:
 */
class RobotStatusReceived(val op: String, val msg: String) : MessageReceivedStrategy {
    override fun dataProcessing() {
        val versionBean = msg.gToBean<VersionSubscribe<Any>>()
        versionBean?.apply {
            if (version == 1) {
                msg.gToBean<VersionSubscribe<WorkStatusSubscribeV1>>()?.apply {
                    content.apply {
                        workStatus.nextWorkStateInfo()
                        electric.nextElectric()
                        Pair(water, sewage).nextWater()
                        WorkStateInfo(
                            workStatusCode,
                            workStatusMessage,
                            emergencyStopStatus
                        ).nextWorkStateInfo()
                        currentExecuteTime.nextCurrentExecuteTime()
                        isCharging.nextIsCharging()
                        if (aromaDiffuser == 0) {
                            false.nextAromaDiffuser()
                        } else {
                            true.nextAromaDiffuser()
                        }
                    }
                }
            }
        }
    }
}

data class WorkStateInfo(
    val workStatusCode: Int,
    val workStatusMessage: String,
    val emergencyStopStatus: Boolean
)