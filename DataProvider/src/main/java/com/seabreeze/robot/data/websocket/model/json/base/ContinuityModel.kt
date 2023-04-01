package com.seabreeze.robot.data.websocket.model.json.base

import android.graphics.PointF
import com.google.gson.annotations.SerializedName
import com.seabreeze.robot.base.ext.foundation.UUIDGenerator

/**
 * User: milan
 * Time: 2022/1/24 12:51
 * Des:
 */

data class RosContinuityTask(
    @SerializedName("continuity_id")
    val id: String = UUIDGenerator.uuid,
    @SerializedName("is_customized")
    val isCustomized: Boolean = false,
    @SerializedName("continuity_list")
    val continuityList: List<RosContinuitySingleTask>
)

data class RosContinuitySingleTask(
    @SerializedName("child_id")
    var id: String = UUIDGenerator.uuid,
    @SerializedName("work_status")
    val workStatus: WorkStatus,//当前状态
    @SerializedName("points")
    val points: List<Float>,
    var name: String = ""
)

data class RosCombinationTask(
    @SerializedName("combination_id")
    var id: String,
    var name: String?
)

data class RosFullPathTask(
    @SerializedName("full_path")
    var fullPath: List<PointF>,
)