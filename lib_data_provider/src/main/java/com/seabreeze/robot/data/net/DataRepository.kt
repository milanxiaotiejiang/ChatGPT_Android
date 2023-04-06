package com.seabreeze.robot.data.net

import com.elvishew.xlog.XLog
import com.google.gson.JsonSyntaxException
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.data.net.api.impl.RobotImpl
import com.seabreeze.robot.data.net.bean.request.RequestChat
import com.seabreeze.robot.data.net.bean.request.RequestImage
import com.seabreeze.robot.data.net.bean.response.ChatMajor
import com.seabreeze.robot.data.net.bean.response.ResponseChat
import com.seabreeze.robot.data.net.bean.response.ResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okio.IOException
import java.util.*

/**
 * User: milan
 * Time: 2020/4/9 17:29
 * Des:
 */
class DataRepository private constructor() {

    init {
        sRobotImplement = RobotImpl()
    }

    companion object {

        private lateinit var sRobotImplement: RobotImpl

        val INSTANCE: DataRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DataRepository()
        }
    }

    suspend fun models(): ResponseModel = sRobotImplement.models()

    suspend fun getCompletion(requestChat: RequestChat): Flow<ChatMajor> = flow {

        val responseBody = sRobotImplement.completions(requestChat)

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
                            var responseChat: ResponseChat? = null
                            try {
                                responseChat = jsonData.gToBean<ResponseChat>()
                            } catch (ex: JsonSyntaxException) {
                                XLog.e("Error parsing json, json is  $jsonData")
                            }

                            responseChat?.run {
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
                                            ChatMajor(
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
                        ChatMajor(
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

    suspend fun generateImage(prompt: String): Flow<String> = flow {
        val responseImage = sRobotImplement.generations(
            RequestImage(
                prompt = prompt,
//                size = "${screenWidth}x$screenHeight"
                size = "${1024}x${1024}"
            )
        )

        emit(responseImage.data[0].url)

    }.flowOn(Dispatchers.IO)

}