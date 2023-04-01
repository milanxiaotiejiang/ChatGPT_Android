package com.seabreeze.robot.data.net.api

import com.seabreeze.robot.data.net.bean.request.RequestChat
import com.seabreeze.robot.data.net.bean.request.RequestImage
import com.seabreeze.robot.data.net.bean.response.ResponseImage
import okhttp3.ResponseBody

/**
 * User: milan
 * Time: 2020/4/9 14:13
 * Des:
 */
interface RobotAPI {
    suspend fun completions(requestChat: RequestChat): ResponseBody
    suspend fun generations(requestImage: RequestImage): ResponseImage
}