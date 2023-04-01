package com.seabreeze.robot.data.net.api.impl

import com.seabreeze.robot.data.net.BaseImpl
import com.seabreeze.robot.data.net.api.RobotAPI
import com.seabreeze.robot.data.net.bean.request.RequestChat
import com.seabreeze.robot.data.net.bean.request.RequestImage
import com.seabreeze.robot.data.net.service.RobotService


/**
 * User: milan
 * Time: 2020/4/9 17:32
 * Des:
 */
class RobotImpl : BaseImpl<RobotService>(), RobotAPI {
    override suspend fun completions(requestChat: RequestChat) =
        mService.completions(requestChat)

    override suspend fun generations(requestImage: RequestImage) =
        mService.generations(requestImage)
}