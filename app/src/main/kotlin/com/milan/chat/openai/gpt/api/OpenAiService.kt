package com.milan.chat.openai.gpt.api

import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

/**
 * User: milan
 * Time: 2023/3/30 19:54
 * Des:
 */

// OpenAiService.kt

interface OpenAiService {
    @Streaming
    @POST("v1/chat/completions")
    suspend fun getCompletion(@Body requestData: RequestData): ResponseBody
}
