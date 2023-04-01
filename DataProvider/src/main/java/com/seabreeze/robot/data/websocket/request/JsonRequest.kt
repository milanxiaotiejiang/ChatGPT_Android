package com.seabreeze.robot.data.websocket.request

import android.os.Looper
import androidx.annotation.StringDef
import com.elvishew.xlog.XLog
import com.google.gson.reflect.TypeToken
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.Platform
import com.seabreeze.robot.base.ext.foundation.otherwise
import com.seabreeze.robot.base.ext.foundation.yes
import com.seabreeze.robot.base.ext.tool.GsonExt.gson
import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.base.ext.tool.gToJson
import com.seabreeze.robot.data.R
import com.seabreeze.robot.data.websocket.RosWebSocketManager
import com.seabreeze.robot.data.websocket.model.json.Response
import com.seabreeze.robot.data.websocket.model.json.base.RequestModel
import com.seabreeze.robot.data.websocket.model.json.base.RosRequest
import com.seabreeze.robot.data.websocket.model.json.base.RosResponse
import com.seabreeze.robot.data.websocket.request.callback.IJsonRequestCallback
import java.lang.reflect.Type

/**
 * User: milan
 * Time: 2021/9/3 16:24
 * Des:
 */
open class JsonRequest<T, R>(private val realRequest: RosRequest<T>) :
    Request(RequestType.PUBLISH, "") {

    override fun transformToRequest(): String {
        val requestModel = RequestModel(
            RequestOp.PUBLISH,
            RequestTopic.APP_JSON,
            realRequest
        )
        return requestModel.gToJson()
    }

    fun analysis(
        requestRecord: Request,
        afferentMsg: String,
        front: () -> Unit,
        after: () -> Unit
    ) {
        val response = afferentMsg.gToBean<Response>()
        response?.apply {
            XLog.e("responseJsonString : $msg")
            val rosResponse = msg.gToBean<RosResponse<Any>>()
            rosResponse?.apply {
                val recordRosRequest = requestRecord.msg.gToBean<RosRequest<Any>>()
                recordRosRequest?.apply {
                    if (recordRosRequest.id == rosResponse.id) {
                        val realRosResponse = gson.fromJson<RosResponse<R>>(
                            msg, mRosModeType
                        )
                        realRosResponse?.apply {
                            if (RosWebSocketManager.INSTANCE.isAppTest) {
                                result = true
                            }
                            result.yes {
                                front()
                                params.run { resultJsonSuccess(this) }
                                after()
                            }.otherwise {
                                front()
                                resultFail(BaseThrowable.InsideThrowable(errorCode, errorMessage))
                                after()
                            }
                        }
                    }
                }
            }
        }

    }

    var mRosModeType: Type? = null

    inline fun <reified R> setTypeToken() {
        mRosModeType = object : TypeToken<RosResponse<R>>() {}.type
    }

    fun resultJsonSuccess(params: R?) {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            Platform.get().execute {
                mCallback?.apply {
                    if (this is IJsonRequestCallback<*>) {
                        val callback = this as IJsonRequestCallback<R>
                        callback.apply {
                            onAfter()
                            onJsonSuccess(params)
                        }
                    }
                }
            }
        } else {
            mCallback?.apply {
                if (this is IJsonRequestCallback<*>) {
                    val callback = this as IJsonRequestCallback<R>
                    callback.apply {
                        onAfter()
                        onJsonSuccess(params)
                    }
                }
            }
        }
    }

    @StringDef(value = [RequestTopic.APP_JSON])
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class RequestTopic {
        companion object {
            const val APP_JSON = "/app_json" //普通发送
        }
    }
}