package com.seabreeze.robot.data.net

import com.elvishew.xlog.XLog
import com.google.gson.JsonSyntaxException
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.data.net.api.ImageAPI
import com.seabreeze.robot.data.net.api.impl.ImageImpl
import com.seabreeze.robot.data.net.api.impl.RobotImpl
import com.seabreeze.robot.data.net.bean.request.RequestData
import com.seabreeze.robot.data.net.bean.response.ConciseData
import com.seabreeze.robot.data.net.bean.response.ResponseData
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
class DataRepository private constructor() : ImageAPI {

    init {
        sRobotImplement = RobotImpl()
        sImageImplement = ImageImpl()
    }

    companion object {

        private lateinit var sRobotImplement: RobotImpl
        private lateinit var sImageImplement: ImageImpl

        val INSTANCE: DataRepository by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DataRepository()
        }
    }

    override fun downloadPicFromNet(fileUrl: String) = sImageImplement.downloadPicFromNet(fileUrl)

    suspend fun getCompletion(
        requestData: RequestData
    ): Flow<ConciseData> = flow {

        val responseBody = sRobotImplement.completions(requestData)

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

}