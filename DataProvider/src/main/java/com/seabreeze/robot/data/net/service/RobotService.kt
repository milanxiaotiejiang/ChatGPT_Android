package com.seabreeze.robot.data.net.service

import com.seabreeze.robot.data.net.bean.request.RequestData
import okhttp3.ResponseBody
import retrofit2.http.*


/**
 * User: milan
 * Time: 2020/4/9 14:13
 * Des:
 */
interface RobotService {
    @Streaming
    @POST("v1/chat/completions")
    suspend fun completions(@Body requestData: RequestData): ResponseBody
}