package com.seabreeze.robot.data.net.service

import com.seabreeze.robot.data.net.bean.request.RequestChat
import com.seabreeze.robot.data.net.bean.request.RequestImage
import com.seabreeze.robot.data.net.bean.response.ResponseImage
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
    suspend fun completions(@Body requestChat: RequestChat): ResponseBody

    @POST("v1/images/generations")
    suspend fun generations(@Body requestImage: RequestImage): ResponseImage

}