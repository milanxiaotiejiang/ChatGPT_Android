package com.seabreeze.robot.data.websocket.model.json.base

import com.google.gson.annotations.SerializedName

/**
 * 清洁记录
 */
data class CleanDataHistoryModel(
    @SerializedName("is_complete")
    val isComplete: Boolean,//清洁状态-对应图标
    @SerializedName("launch_time")
    val launchTime: Long,//发起时间
    @SerializedName("execute_time")
    var executeTime: Long,//执行时间
    @SerializedName("end_time")
    val endTime: Long,//时间
    @SerializedName("task_mode")
    val taskMode: Int,//任务类型
    @SerializedName("task_type")
    val taskType: Int,//任务发起端（1——Pad；2——定时任务；3——App）
    @SerializedName("work_status")
    val workStatus: WorkStatus,//当前状态
    @SerializedName("clean_area")
    val cleanArea: Float,//面积
    @SerializedName("clean_time")
    val cleanTime: Int,//用时
    @SerializedName("error_code")
    val errorCode: Int,//错误码
    @SerializedName("error_message")
    val errorMessage: String,//错误信息
)