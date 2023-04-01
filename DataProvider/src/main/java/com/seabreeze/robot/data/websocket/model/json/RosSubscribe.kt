package com.seabreeze.robot.data.websocket.model.json

import android.graphics.PointF
import com.google.gson.annotations.SerializedName
import com.seabreeze.robot.base.ext.foundation.UUIDGenerator
import com.seabreeze.robot.data.websocket.model.json.base.Header
import com.seabreeze.robot.data.websocket.model.json.base.WorkStatus

/**
 * User: milan
 * Time: 2021/11/25 13:03
 * Des:
 */
data class VersionSubscribe<T>(
    val version: Int,
    val content: T
)

data class WorkStatusSubscribeV1(
    val electric: Int,
    val water: Int,
    val sewage: Int,
    val aromaDiffuser: Int,
    @SerializedName("work_status_code")
    val workStatusCode: Int,
    @SerializedName("work_status_message")
    val workStatusMessage: String,
    @SerializedName("emergency_stop_status")
    val emergencyStopStatus: Boolean,
    @SerializedName("current_execute_time")
    val currentExecuteTime: Long,
    @SerializedName("is_charging")
    val isCharging: Boolean,
    @SerializedName("work_status")
    val workStatus: WorkStatus,
)

data class MaterialStatusSubscribeV1(
    @SerializedName("carpet_brush")
    val carpetBrush: Duration,
    @SerializedName("fan_filter")
    val fanFilter: Duration,
    @SerializedName("push_brush")
    val pushBrush: Duration,
    @SerializedName("soft_brush")
    val softBrush: Duration,
)

data class Duration(
    @SerializedName("expected_duration")
    val expectedDuration: Long,//预计使用时间，单位：秒
    @SerializedName("use_duration")
    val useDuration: Long,//已使用时间，单位：秒
)

data class ErrorSubscribe(
    @SerializedName("error_code")
    val errorCode: Int,
    @SerializedName("error_message")
    val errorMessage: String,
)

data class NoticeSubscribe(
    @SerializedName("notice_code")
    val noticeCode: Int,
    @SerializedName("notice_time")
    val noticeTime: Long,
    @SerializedName("notice_title")
    val noticeTitle: String,
    @SerializedName("notice_message")
    val noticeMessage: String,
    @SerializedName("solution")
    val solution: String,
) {
    companion object {
        const val OTA_UPDATE_CODE = 6666
    }
}

data class RosTaskPoint(
    val id: String = UUIDGenerator.uuid,
    var x: Float,
    var y: Float,
    val totalStep: Int,
    val currentStep: Int,
    val totalFrequency: Int,
    val currentFrequency: Int,
    @SerializedName("work_status")
    val workStatus: WorkStatus,
    val plannerType: Int,
    @SerializedName("is_cleaning")
    val inCleaning: Boolean,
    val mode: Int,
) {
    override fun toString(): String {
        return "RosTaskPoint(id='$id', totalStep=$totalStep, currentStep=$currentStep, totalFrequency=$totalFrequency, currentFrequency=$currentFrequency)"
    }
}

data class DrawTaskPoint(
    val point: RosTaskPoint,
    var rx: Float,
    var ry: Float,
    var work: Boolean,
    val plannerType: Int,
    val inCleaning: Boolean
)

data class RosScanLaser(
    val header: Header,
    val angle_min: Float,
    val angle_max: Float,
    val angle_increment: Float,
    val time_increment: Float,
    val scan_time: Float,
    val range_min: Float,
    val range_max: Float,
    val ranges: List<Float>, // x坐标
    val intensities: List<Float> // y坐标
)

data class SimpleRosScanLaser(
    val realList: List<PointF>
)

data class GlobalPath(
    val header: Header,
    val poses: List<Pose>
)