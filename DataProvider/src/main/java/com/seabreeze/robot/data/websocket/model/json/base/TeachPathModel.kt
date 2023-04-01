package com.seabreeze.robot.data.websocket.model.json.base

import android.graphics.PointF
import com.google.gson.annotations.SerializedName
import com.seabreeze.robot.base.ext.foundation.UUIDGenerator

/**
 * User: milan
 * Time: 2022/1/24 12:52
 * Des:
 */

data class RosTeachPathTask(
    @SerializedName("teach_path_id")
    val id: String,
    @SerializedName("teach_point_list")
    val teachPointList: List<PointF> = mutableListOf()
)

data class RosTeachPath(
    @SerializedName("teach_path_id")
    val id: String = UUIDGenerator.uuid,
    @SerializedName("teach_path_name")
    val name: String = "",
    @SerializedName("force_quit")
    val forceQuit: Boolean = false,
)

data class RosTeachPathDetail(
    val id: String = UUIDGenerator.uuid,
    val name: String = "",
    val realList: List<PointF> = arrayListOf(),
    var isSelect: Boolean = false,
    var incidentalId: String = UUIDGenerator.uuid,
)