package com.milan.chat.openai.gpt.api

import com.elvishew.xlog.XLog
import com.google.gson.JsonSyntaxException
import com.milan.chat.openai.gpt.ext.gToBean
import com.milan.chat.openai.gpt.model.BaseThrowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import java.util.*

/**
 * User: milan
 * Time: 2023/3/31 16:44
 * Des:
 */
class OpenAiRespiratory private constructor() {

    private val openAiService: OpenAiService by lazy {
        RetrofitClient.openAiService
    }

//    suspend fun getCompletion(
//        requestData: RequestData, streamCallback: (conciseData: ConciseData) -> Unit
//    ) {
//        withContext(Dispatchers.IO) {
//
//            val requestJson = requestData.gToJson()
//            val mediaType = "application/json; charset=utf-8".toMediaType()
//            val requestBody = requestJson.toRequestBody(mediaType)
//            val request = Request.Builder().url("https://api.openai.com/v1/chat/completions")
//                .post(requestBody).build()
//
//            val call = RetrofitClient.okHttpClient.newCall(request)
//            val response = call.execute()
//            if (!response.isSuccessful) {
//                throw IOException("Unexpected HTTP code ${response.code}")
//            }
//
//            val responseBody = response.body ?: throw IOException("Response body is null")
//
//            val source = responseBody.source()
//            source.request(Long.MAX_VALUE)
//
//            source.buffer.use { buffer ->
//                while (!buffer.exhausted()) {
//                    val line = buffer.readUtf8Line()
//                    if (!line.isNullOrBlank() && line.startsWith("data: ")) {
//                        val jsonData = line.substringAfter("data: ")
//                        if (jsonData != "[DONE]") {
//                            var responseData: ResponseData? = null
//                            try {
//                                responseData = jsonData.gToBean<ResponseData>()
//                            } catch (ex: JsonSyntaxException) {
//                                XLog.e("Error parsing json, json is  $jsonData")
//                            }
//                            responseData?.choices?.firstOrNull()?.apply {
//                                delay(100)
//                                val conciseData = ConciseData(
//                                    responseData.id, 0, delta.role, delta.content
//                                )
//                                streamCallback.invoke(conciseData)
//                            }
//                        }
//                    }
//                }
//            }
//            responseBody.close()
//        }
//    }

    suspend fun getCompletion(
        requestData: RequestData
    ): Flow<ConciseData> = flow {

        val responseBody = openAiService.getCompletion(requestData)

        val source = responseBody.source()
        source.request(Long.MAX_VALUE)

        var hasError = true
        var currentId: String = UUID.randomUUID().leastSignificantBits.toString()
        var currentRole: String? = null
        val messageStringBuilder = StringBuilder()
        var index = 0
        try {
            source.buffer.use { buffer ->
                while (!buffer.exhausted()) {
                    val line = buffer.readUtf8Line()
                    if (!line.isNullOrBlank() && line.startsWith("data: ")) {
                        val jsonData = line.substringAfter("data: ")
                        if (jsonData != "[DONE]") {
                            var responseData: ResponseData? = null
                            try {
                                responseData = jsonData.gToBean<ResponseData>()
                            } catch (ex: JsonSyntaxException) {
                                XLog.e("Error parsing json, json is  $jsonData")
                            }

                            responseData?.run {
                                currentId = id

                                choices.firstOrNull()?.apply {
                                    if (finish_reason == "stop") {
                                        hasError = false
                                    } else {

                                        delay(100)

                                        if (currentRole == null) {
                                            currentRole = delta.role
                                        }

                                        delta.content?.apply {
                                            messageStringBuilder.append(this)
                                        }

                                        emit(
                                            ConciseData(
                                                currentId,
                                                index,
                                                currentRole,
                                                messageStringBuilder.toString()
                                            )
                                        )
                                        index++
                                    }
                                }

                            }
                        }
                    }
                }
                if (hasError) {
                    throw BaseThrowable.ExternalThrowable(IOException("Not all acquisition completed"))
                } else {
                    emit(
                        ConciseData(
                            currentId,
                            index,
                            currentRole,
                            messageStringBuilder.toString(),
                            true
                        )
                    )
                }
            }
        } catch (e: IOException) {
            XLog.e("Error reading response body: ${e.message}")
            throw BaseThrowable.ExternalThrowable(e)
        } catch (e: Exception) {
            XLog.e("Error emitting flow: ${e.message}")
            throw BaseThrowable.ExternalThrowable(e)
        } finally {
            responseBody.close()
        }
    }.flowOn(Dispatchers.IO)


    companion object {
        val INSTANCE: OpenAiRespiratory by lazy { OpenAiRespiratory() }
    }
}