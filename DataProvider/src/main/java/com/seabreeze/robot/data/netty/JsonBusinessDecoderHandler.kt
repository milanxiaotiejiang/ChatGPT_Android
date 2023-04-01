package com.seabreeze.robot.data.netty

import com.elvishew.xlog.XLog
import com.orhanobut.logger.Logger
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.data.websocket.AuthThrowable
import com.seabreeze.robot.data.websocket.RosSubjectCenter.nextReceived
import com.seabreeze.robot.data.websocket.request.Request
import com.seabreeze.robot.data.websocket.request.RequestTopicManager
import com.seabreeze.robot.data.websocket.response.*
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import io.netty.handler.codec.MessageToMessageDecoder
import org.json.JSONObject
import java.text.DecimalFormat

/**
 * User: milan
 * Time: 2022/2/18 12:38
 * Des: 业务处理解码类
 * 处理 Json Str 消息
 */
@ChannelHandler.Sharable
class JsonBusinessDecoderHandler :
    MessageToMessageDecoder<String>() {

//    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
//        super.userEventTriggered(ctx, evt)
//        if (evt === IdleStateEvent.READER_IDLE_STATE_EVENT) {
//            mAuthPromise?.setFailure(SocketOvertimeThrowable())
//        }
//    }


    var mAuthPromise: ChannelPromise? = null

    private var FORMAT = DecimalFormat("0.00")

    override fun decode(ctx: ChannelHandlerContext, decodeMsg: String, out: MutableList<Any>?) {
        XLog.i("JsonBusinessDecoderHandler decodeMsg size :  ${FORMAT.format(decodeMsg.toByteArray().size / 1024.0)} KB")

        try {
            val jsonObject = JSONObject(decodeMsg)
            val topic = jsonObject.getString("topic")
            val op = jsonObject.getString("op")
            val msg = jsonObject.getString("msg")
            onMessageReceived(topic, op, msg)
        } catch (e: Exception) {
            onMessageReceivedError(BaseThrowable.ExternalThrowable(e))
        }
    }

    private fun onMessageReceived(topic: String, op: String, msg: String) {
        var messageReceivedStrategy: MessageReceivedStrategy? = null
        when (op) {
            Request.RequestOp.PUBLISH -> {
                when (topic) {
                    RequestTopicManager.map() -> {
//                        val response = msg.gToBean<Response>()
//                        response?.apply {
//                            messageReceivedStrategy = MapReceived(data)
//                        }
//                        XLog.e("JsonBusinessDecoderHandler map ${msg.toByteArray().size}")
                        messageReceivedStrategy = MapReceived(msg)
                    }
                    RequestTopicManager.odom() -> {
                        messageReceivedStrategy = OdomReceived(msg)
                    }
                    RequestTopicManager.robotStatus() -> {
                        messageReceivedStrategy = RobotStatusReceived(op, msg)
                    }
                    RequestTopicManager.errorApp() -> {
                        messageReceivedStrategy = ErrorReceived(msg)
                    }
                    RequestTopicManager.noticeApp() -> {
                        messageReceivedStrategy = NoticeReceived(msg)
                    }
                    RequestTopicManager.taskPoint() -> {
                        messageReceivedStrategy = TaskPointReceived(msg)
                    }
                    RequestTopicManager.scanLaser() -> {
                        messageReceivedStrategy = ScanLaserReceived(msg)
                    }
                    RequestTopicManager.checkApp() -> {
                        messageReceivedStrategy = CheckAppReceived(msg)
                    }
                    RequestTopicManager.pathPlanningNode() -> {
                        messageReceivedStrategy = GlobalReceived(msg)
                    }
                    RequestTopicManager.materialStatus() -> {
                        messageReceivedStrategy = MaterialStatusReceived(op, msg)
                    }
                    RequestTopicManager.response() -> {
                        XLog.e("response topic : $topic , op : $op , msg : $msg")
                    }
                    RequestTopicManager.responseJSON() -> {
                        XLog.w("responseJSON topic : $topic , op : $op , msg : $msg")
                        RequestFuturePool.setResultInResponseJSON(msg)
                    }
                    else -> {
                        XLog.e("else topic : $topic , op : $op , msg : $msg")
                    }
                }
            }
            Request.RequestOp.SUBSCRIBE -> {
                XLog.e("topic : $topic , op : $op , msg : $msg")
            }
            Request.RequestOp.AUTH -> {
                XLog.e("topic : $topic , op : $op , msg : $msg")
                if (msg == NettyImpl.TEST_PASS) {
                    mAuthPromise?.setSuccess()
                } else {
                    mAuthPromise?.setFailure(AuthThrowable())
                }
            }
            else -> msg.nextReceived()
        }

        messageReceivedStrategy?.apply {
            val messageReceived = MessageReceived(this)
            messageReceived.startDataProcessing()
        }

    }

    private fun onMessageReceivedError(externalThrowable: BaseThrowable.ExternalThrowable) {
        Logger.e(externalThrowable.message.toString())
    }
}