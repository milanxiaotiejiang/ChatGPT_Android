package com.seabreeze.robot.data.websocket.model.json

import com.seabreeze.robot.base.ext.tool.screenWidth
import com.seabreeze.robot.data.websocket.model.json.base.Header
import com.seabreeze.robot.data.websocket.model.json.base.Orientation
import com.seabreeze.robot.data.websocket.model.json.base.Position
import com.seabreeze.robot.data.websocket.model.json.base.Stamp

/**
 * User: milan
 * Time: 2021/9/3 9:53
 * Des:
 */

fun loadShowMap(multiple: Int = 1): RosMapData {
    val width = (screenWidth / 2 * 1.5 * multiple).toInt()
    val height = (screenWidth / 2 * multiple).toInt()

    val `data` = mutableListOf<Int>()
    `data`.add(1)
    `data`.add(width * height)

    val info = Info(
        height,
        MapLoadTime(0, 0),
        Origin(
            Orientation(0.0, 0.0, 0.0, 0.0),
            Position(0.0, 0.0, 0.0)
        ),
        0.0,
        width
    )

    return RosMapData(
        `data`,
        Header(
            "",
            0,
            Stamp(0, 0)
        ),
        info
    )
}

data class RosMapData(
    val `data`: MutableList<Int>,
    val header: Header,
    val info: Info
)

data class Info(
    val height: Int,
    val map_load_time: MapLoadTime,
    val origin: Origin,
    val resolution: Double,
    val width: Int
)

data class MapLoadTime(
    val nsecs: Int,
    val secs: Int
)

data class Origin(
    val orientation: Orientation,
    val position: Position
)

data class MultiThreadingRecord(
    var originX: Double,
    var originY: Double,
    var resolution: Double,
    var isChange: Boolean = false
) {
    override fun toString(): String {
        return "MultiThreadingRecord(originX=$originX, originY=$originY, resolution=$resolution)"
    }
}
