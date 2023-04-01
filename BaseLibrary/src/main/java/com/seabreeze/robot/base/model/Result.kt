package com.seabreeze.robot.base.model

data class BaseResult<T>(
    val code: Int,
    val message: String,
    val success: Boolean,
    val timestamp: Long,
    val result: T
) {
    override fun toString(): String {
        return "BaseResult(code=$code, message='$message', success=$success, timestamp=$timestamp, result=$result)"
    }
}

data class ListModel<T>(
    val showData: List<T>? = null,
    val showError: String? = null,
    val showEnd: Boolean = false, // 加载更多
    val isRefresh: Boolean = false, // 刷新
    val isFinishRefresh: Boolean = true
)

data class Pager<T>(
    val curPage: Int,
    val datas: List<T>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)