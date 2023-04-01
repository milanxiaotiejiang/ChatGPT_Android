package com.seabreeze.robot.data.websocket.request

import androidx.annotation.StringDef
import com.seabreeze.robot.base.ext.tool.gToJson
import com.seabreeze.robot.data.websocket.model.json.base.PublishRequestModel
import com.seabreeze.robot.data.websocket.model.json.base.RequestModel

/**
 * User: milan
 * Time: 2021/9/3 16:24
 * Des:
 */
class OneWayRequest(msg: String) : Request(RequestType.ONE_WAY, msg) {

    override fun transformToRequest(): String {
        val requestModel = RequestModel(
            RequestOp.PUBLISH,
            RequestTopic.APP_COMMUNICATION,
            PublishRequestModel(msg)
        )
        return requestModel.gToJson()
    }

    @StringDef(value = [RequestTopic.APP_COMMUNICATION])
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class RequestTopic {
        companion object {
            const val APP_COMMUNICATION = "/app_communication" //普通发送
        }
    }
}