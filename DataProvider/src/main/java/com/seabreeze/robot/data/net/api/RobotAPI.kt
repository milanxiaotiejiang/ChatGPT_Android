package com.seabreeze.robot.data.net.api

import com.seabreeze.robot.data.net.bean.request.RequestData
import okhttp3.ResponseBody

/**
 * User: milan
 * Time: 2020/4/9 14:13
 * Des:
 */
interface RobotAPI {
    suspend fun completions(requestData: RequestData): ResponseBody
}