package com.seabreeze.robot.data.websocket.model.json.base

import android.graphics.Point

/**
 * User: milan
 * Time: 2022/9/17 9:52
 * Des:
 */
object RosRoomModel {
    const val FULL_CLEANING_PATH = -1

    const val POSE_DATA_MODE = 1
    const val POINT_DATA_MODE = 2

    const val DEFAULT_EXPLORER_MODE = 0
    const val UNCOMMON_EXPLORER_MODE = 1
}

data class RoomInfo(
    val id: Int,
    val name: String,
    val center: Point,
    val memberPoints: List<Point>,
    val neighborRoomIds: List<Int>,
    val area: Double,
    val perimeter: Double
) : Cloneable {
    override fun toString(): String {
        return "MapRoom(id=$id, memberPoints=${memberPoints.size}, neighborRoomIds=$neighborRoomIds, area=$area, perimeter=$perimeter)"
    }
}

data class MapRoom(
    val width: Int,
    val height: Int,
    val rooms: List<RoomInfo>
)

data class RoomMergeTarget(
    val targetId: Int,
    val mergeId: Int
)

data class RoomSegmentationTarget(
    val targetId: Int,
    val ps: Point,
    val pe: Point
)

data class RoomNameTarget(
    val targetId: Int,
    val name: String
)

data class RoomExplorationTarget(
    val partition: Boolean,//是否是分区,true代表是分区，需要传targetId
    val targetId: Int,
    val dataMode: Int = RosRoomModel.POSE_DATA_MODE,
    var explorerMode: Int = RosRoomModel.DEFAULT_EXPLORER_MODE
)

data class RoomCoverage(
    val coverageId: String,//uuid
    val poseList: List<PosePoint>,
    val pointList: List<Point>
)

data class PosePoint(
    val x: Float,
    val y: Float,
    val theta: Float
)