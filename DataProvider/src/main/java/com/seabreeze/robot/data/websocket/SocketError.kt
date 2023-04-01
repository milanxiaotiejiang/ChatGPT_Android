package com.seabreeze.robot.data.websocket

import com.seabreeze.robot.base.ext.foundation.BaseThrowable

/**
 * User: milan
 * Time: 2021/12/20 12:15
 * Des:
 */
class SocketUnInitThrowable : BaseThrowable.InsideThrowable(9000, "未初始化")
class SocketSendInsideThrowable : BaseThrowable.InsideThrowable(9001, "发送失败")
class SocketReconnectThrowable : BaseThrowable.InsideThrowable(9002, "断线重连中")
class SocketConnectingThrowable : BaseThrowable.InsideThrowable(9003, "正在连接中")
class SocketDisconnectedThrowable : BaseThrowable.InsideThrowable(9004, "已断开连接，请重新连接")

class SocketUnknownThrowable : BaseThrowable.InsideThrowable(9999, "未知错误")

class SocketOvertimeThrowable : BaseThrowable.InsideThrowable(9010, "发送超时")

class DisconnectedThrowable : BaseThrowable.InsideThrowable(9011, "连接已断开")
class ChannelThrowable(errorMessage: String) : BaseThrowable.InsideThrowable(9012, errorMessage)
class AuthThrowable : BaseThrowable.InsideThrowable(9013, "认证失败")
class AnalysisThrowable : BaseThrowable.InsideThrowable(9014, "解析失败")

class DataErrorThrowable : BaseThrowable.InsideThrowable(-200, "数据错误")

class NebulaErrorThrowable(errorCode: Int, errorMessage: String) : BaseThrowable.InsideThrowable(errorCode, errorMessage)
