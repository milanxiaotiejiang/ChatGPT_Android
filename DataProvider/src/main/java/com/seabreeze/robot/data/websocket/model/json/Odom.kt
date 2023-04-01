package com.seabreeze.robot.data.websocket.model.json

import com.seabreeze.robot.data.websocket.model.json.base.Header
import com.seabreeze.robot.data.websocket.model.json.base.Orientation
import com.seabreeze.robot.data.websocket.model.json.base.Position

/**
 * User: milan
 * Time: 2021/9/6 13:05
 * Des:
 */
data class Odom(
    val child_frame_id: String,
    val header: Header,
    val pose: Pose,
    val twist: Twist
)

data class Pose(
    val covariance: List<Float>,
    val pose: PoseX
)

data class Twist(
    val covariance: List<Float>,
    val twist: TwistX
)

data class PoseX(
    val orientation: Orientation,
    val position: Position
)

data class TwistX(//{"linear":{"y":0,"x":0,"z":0},"angular":{"y":0,"x":0,"z":2}}
    val angular: Angular,
    val linear: Linear
)

data class Angular(
    val x: Float,
    val y: Float,
    val z: Float
)

data class Linear(
    val x: Float,
    val y: Float,
    val z: Float
)