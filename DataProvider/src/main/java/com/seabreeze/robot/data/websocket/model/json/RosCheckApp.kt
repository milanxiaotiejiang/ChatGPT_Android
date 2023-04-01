package com.seabreeze.robot.data.websocket.model.json

import com.google.gson.annotations.SerializedName

/**
 * User: milan
 * Time: 2022/8/1 15:18
 * Des:
 */

data class RosCheckAppV1(
    @SerializedName("camera_check")//
    val cameraCheck: Boolean,

    @SerializedName("fall_tof_front_left_check")//
    val fallTofFrontLeftCheck: Boolean,
    @SerializedName("fall_tof_front_left_value")//
    val fallTofFrontLeftValue: Float,

    @SerializedName("fall_tof_front_right_check")//
    val fallTofFrontRightCheck: Boolean,
    @SerializedName("fall_tof_front_right_value")//
    val fallTofFrontRightValue: Float,

    @SerializedName("fall_tof_left_check")//
    val fallTofLeftCheck: Boolean,
    @SerializedName("fall_tof_left_value")//
    val fallTofLeftValue: Float,

    @SerializedName("fall_tof_right_check")//
    val fallTofRightCheck: Boolean,
    @SerializedName("fall_tof_right_value")//
    val fallTofRightValue: Float,

    @SerializedName("laser_check")//
    val laserCheck: Boolean,

    @SerializedName("supersonic_corner_left_check")
    val supersonicCornerLeftCheck: Boolean,
    @SerializedName("supersonic_corner_left_value")
    val supersonicCornerLeftValue: Float,
    @SerializedName("supersonic_corner_right_check")
    val supersonicCornerRightCheck: Boolean,
    @SerializedName("supersonic_corner_right_value")
    val supersonicCornerRightValue: Float,

    @SerializedName("supersonic_front_left_check")
    val supersonicFrontLeftCheck: Boolean,
    @SerializedName("supersonic_front_left_value")
    val supersonicFrontLeftValue: Float,

    @SerializedName("supersonic_front_right_check")
    val supersonicFrontRightCheck: Boolean,
    @SerializedName("supersonic_front_right_value")
    val supersonicFrontRightValue: Float,

    @SerializedName("supersonic_left_check")
    val supersonicLeftCheck: Boolean,
    @SerializedName("supersonic_left_value")
    val supersonicLeftValue: Float,

    @SerializedName("supersonic_right_check")
    val supersonicRightCheck: Boolean,
    @SerializedName("supersonic_right_value")
    val supersonicRightValue: Float,

    @SerializedName("welt_tof_after_check")
    val weltTofAfterCheck: Boolean,
    @SerializedName("welt_tof_after_value")
    val weltTofAfterValue: Float,

    @SerializedName("welt_tof_front_check")
    val weltTofFrontCheck: Boolean,
    @SerializedName("welt_tof_front_value")
    val weltTofFrontValue: Float,
)


data class RosCheckAppV2(
    @SerializedName("camera_check")//
    val cameraCheck: Boolean,

    @SerializedName("fall_tof_front_left_check")//
    val fallTofFrontLeftCheck: Boolean,
    @SerializedName("fall_tof_front_left_value")//
    val fallTofFrontLeftValue: Float,

    @SerializedName("fall_tof_front_right_check")//
    val fallTofFrontRightCheck: Boolean,
    @SerializedName("fall_tof_front_right_value")//
    val fallTofFrontRightValue: Float,

    @SerializedName("fall_tof_left_check")//
    val fallTofLeftCheck: Boolean,
    @SerializedName("fall_tof_left_value")//
    val fallTofLeftValue: Float,

    @SerializedName("fall_tof_right_check")//
    val fallTofRightCheck: Boolean,
    @SerializedName("fall_tof_right_value")//
    val fallTofRightValue: Float,

    @SerializedName("laser_check")//
    val laserCheck: Boolean,

    @SerializedName("supersonic_corner_left_check")
    val supersonicCornerLeftCheck: Boolean,
    @SerializedName("supersonic_corner_left_value")
    val supersonicCornerLeftValue: Float,
    @SerializedName("supersonic_corner_right_check")
    val supersonicCornerRightCheck: Boolean,
    @SerializedName("supersonic_corner_right_value")
    val supersonicCornerRightValue: Float,

    @SerializedName("supersonic_front_left_check")
    val supersonicFrontLeftCheck: Boolean,
    @SerializedName("supersonic_front_left_value")
    val supersonicFrontLeftValue: Float,

    @SerializedName("supersonic_front_right_check")
    val supersonicFrontRightCheck: Boolean,
    @SerializedName("supersonic_front_right_value")
    val supersonicFrontRightValue: Float,

    @SerializedName("supersonic_left_check")
    val supersonicLeftCheck: Boolean,
    @SerializedName("supersonic_left_value")
    val supersonicLeftValue: Float,

    @SerializedName("supersonic_right_check")
    val supersonicRightCheck: Boolean,
    @SerializedName("supersonic_right_value")
    val supersonicRightValue: Float,

    @SerializedName("welt_tof_after_check")
    val weltTofAfterCheck: Boolean,
    @SerializedName("welt_tof_after_value")
    val weltTofAfterValue: Float,

    @SerializedName("welt_tof_front_check")
    val weltTofFrontCheck: Boolean,
    @SerializedName("welt_tof_front_value")
    val weltTofFrontValue: Float,
)