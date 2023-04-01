package com.seabreeze.robot.data.websocket.model.json.base

/**
 * User: milan
 * Time: 2021/9/3 9:54
 * Des:
 */
data class RequestModel<T>
    (
    val op: String,
    val topic: String,
    val msg: T? = null
)

data class PublishRequestModel(
    val `data`: String
)

data class JsonRequestModel(
    val `data`: String
)


// comment
data class Header(
    val frame_id: String,
    val seq: Int,
    val stamp: Stamp
)

data class Stamp(
    val nsecs: Int,
    val secs: Int
)

data class Orientation(
    val w: Double,
    val x: Double,
    val y: Double,
    val z: Double
)

data class Position(
    val x: Double,
    val y: Double,
    val z: Double
)