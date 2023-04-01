package com.seabreeze.robot.data.websocket.request

import com.seabreeze.robot.base.ext.tool.gToJson
import com.seabreeze.robot.data.websocket.model.json.base.RequestModel

/**
 * User: milan
 * Time: 2021/9/3 16:23
 * Des:
 */
class SubscribeRequest(private val topic: String, msg: String = "") :
    Request(RequestType.SUBSCRIBE, msg) {

    override fun transformToRequest(): String {
        val requestModel = RequestModel<String>(op = RequestOp.SUBSCRIBE, topic = topic, "")
        return requestModel.gToJson()
    }
}

object RequestTopicManager {

    private const val MAP = "/map_app"
    private const val ODOM = "/odom_app"
    private const val ROBOT_STATUS = "/robot_status"
    private const val ERROR_APP = "/error_app"
    private const val NOTICE_APP = "/notice_app"
    private const val TASK_POINT = "/task_point"
    private const val SCAN_APP = "/scan_app"
    private const val CHECK_APP = "/check_app"
    private const val RESPONSE = "/response"
    private const val RESPONSE_JSON = "/response_json"
    private const val PATH_PLANNING_NODE = "/cleaning_path"
    private const val MATERIAL_STATUS = "/material_status"

    fun map() = MAP

    fun odom() = ODOM

    fun robotStatus() = ROBOT_STATUS

    fun errorApp() = ERROR_APP

    fun noticeApp() = NOTICE_APP

    fun taskPoint() = TASK_POINT

    fun scanLaser() = SCAN_APP

    fun checkApp() = CHECK_APP

    fun response() = RESPONSE

    fun responseJSON() = RESPONSE_JSON

    fun pathPlanningNode() = PATH_PLANNING_NODE

    fun materialStatus() = MATERIAL_STATUS

}
