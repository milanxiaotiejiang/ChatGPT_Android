package com.seabreeze.robot.data.websocket.model.json.base

/**
 * User: milan
 * Time: 2022/4/7 9:37
 * Des:
 */
object RosJsonCode {


    //错误码:0 无错误
    const val ROS_SUCCESS_CODE = 0

    //清扫中
    const val ROS_IS_WORKING = 10001

    //充电中
    const val ROS_CHARGING = 10002

    //清扫暂停中
    const val ROS_WORKING_PAUSE = 10003

    //急停中
    const val EMERGENCY_STOP: Int = 10004

    //转场中
    const val ROS_IN_TRANSITION: Int = 10005

    //待机中
    const val ROS_IN_STANDBY = 10006

    //转场暂停中
    const val ROS_IN_TRANSITION_PAUSE: Int = 10007

    //低电量回充
    const val ROS_LOW_POWER = 10008

    //任务完成回充
    const val ROS_TASK_FINISH = 10009

    //dock_error
    const val ROS_DOCK_ERROR = 10010

    //出站中
    const val ROS_OUTBOUND = 10011

    //充电完成
    const val ROS_FINISHED_CHARGE = 10012

    //手动模式
    const val ROS_MANUAL_MODE = 10013

    //入站中
    const val ROS_INBOUND = 10014

}