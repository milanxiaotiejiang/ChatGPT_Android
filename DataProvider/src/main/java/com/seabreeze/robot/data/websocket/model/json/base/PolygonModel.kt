package com.seabreeze.robot.data.websocket.model.json.base

import com.google.gson.annotations.SerializedName
import com.seabreeze.robot.base.ext.foundation.UUIDGenerator

/**
 * User: milan
 * Time: 2022/1/24 12:51
 * Des:
 */

data class RosPolygonTask(
    @SerializedName("polygon_id")
    val id: String = UUIDGenerator.uuid,
    @SerializedName("is_customized")
    val isCustomized: Boolean = false,
    @SerializedName("polygon_list")
    val polygonList: List<RosPolygonSingleTask>
)

data class RosPolygonSingleTask(
    @SerializedName("child_id")
    var id: String = UUIDGenerator.uuid,
    @SerializedName("work_status")
    val workStatus: WorkStatus,//当前状态
    @SerializedName("points")
    val points: List<Float>,
    var name: String?
)