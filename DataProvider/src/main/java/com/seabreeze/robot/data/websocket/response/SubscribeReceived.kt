package com.seabreeze.robot.data.websocket.response

import android.graphics.PointF
import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextCheckApp
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextError
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextGlobalPath
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextMaterialStatus
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextNotice
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextPoint
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextScanLaser
import com.seabreeze.robot.data.websocket.model.json.*
import org.json.JSONObject

/**
 * User: milan
 * Time: 2021/11/25 13:58
 * Des:
 */
class ErrorReceived(val msg: String) : MessageReceivedStrategy {
    override fun dataProcessing() {
        val error = msg.gToBean<ErrorSubscribe>()
        error?.nextError()
    }
}

class NoticeReceived(val msg: String) : MessageReceivedStrategy {
    override fun dataProcessing() {
        val notice = msg.gToBean<NoticeSubscribe>()
        notice?.nextNotice()
    }
}

class TaskPointReceived(val msg: String) : MessageReceivedStrategy {
    override fun dataProcessing() {
        val point = msg.gToBean<RosTaskPoint>()
        point?.nextPoint()
    }
}

class ScanLaserReceived(val msg: String) : MessageReceivedStrategy {
    override fun dataProcessing() {
        val rosScanLaser = msg.gToBean<RosScanLaser>()
        rosScanLaser?.apply {
            if (ranges.size == intensities.size) {
                val realList = mutableListOf<PointF>()
                for (i in ranges.indices) {
                    realList.add(PointF(ranges[i], intensities[i]))
                }
                val simpleRosScanLaser = SimpleRosScanLaser(realList)
                simpleRosScanLaser.nextScanLaser()
            }
        }
    }
}

class CheckAppReceived(val msg: String) : MessageReceivedStrategy {
    override fun dataProcessing() {
        if (DataBackupCenter.INSTANCE.msgCheckApp != msg) {
            val versionBean = msg.gToBean<VersionSubscribe<Any>>()
            versionBean?.apply {
                if (version == 1) {
                    msg.gToBean<VersionSubscribe<RosCheckAppV1>>()?.apply {
                        content.nextCheckApp()
                    }
                } else if (version == 2) {
                    msg.gToBean<VersionSubscribe<RosCheckAppV2>>()?.apply {
                        content.nextCheckApp()
                    }
                }
            }

        }
        DataBackupCenter.INSTANCE.msgCheckApp = msg
    }
}

class GlobalReceived(val msg: String) : MessageReceivedStrategy {
    override fun dataProcessing() {
        val globalPath = msg.gToBean<GlobalPath>()
        globalPath?.nextGlobalPath()
    }

}

class MaterialStatusReceived(val op: String, val msg: String) : MessageReceivedStrategy {
    override fun dataProcessing() {
        val versionBean = msg.gToBean<VersionSubscribe<Any>>()
        versionBean?.apply {
            if (version == 1) {
                msg.gToBean<VersionSubscribe<MaterialStatusSubscribeV1>>()?.apply {
                    content.nextMaterialStatus()
                }
            }
        }
    }
}