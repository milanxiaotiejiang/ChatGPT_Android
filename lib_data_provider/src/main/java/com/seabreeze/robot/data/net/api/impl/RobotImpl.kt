package com.seabreeze.robot.data.net.api.impl

import com.seabreeze.robot.data.net.BaseImpl
import com.seabreeze.robot.data.net.api.RobotAPI
import com.seabreeze.robot.data.net.bean.request.RequestData
import com.seabreeze.robot.data.net.service.RobotService


/**
 * User: milan
 * Time: 2020/4/9 17:32
 * Des:
 */
class RobotImpl : BaseImpl<RobotService>(), RobotAPI {
    override suspend fun completions(requestData: RequestData) = mService.completions(requestData)
}