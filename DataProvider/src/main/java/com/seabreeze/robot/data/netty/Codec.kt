package com.seabreeze.robot.data.netty

import com.orhanobut.logger.Logger
import com.seabreeze.robot.base.common.AppContext
import com.seabreeze.robot.base.ext.tool.gToJson
import com.seabreeze.robot.base.ext.tool.getAppVersionName
import com.seabreeze.robot.data.websocket.SocketOvertimeThrowable
import com.seabreeze.robot.data.websocket.model.hex.base.PDU
import com.seabreeze.robot.data.websocket.model.json.base.RequestModel
import com.seabreeze.robot.data.websocket.request.PublishRequest
import com.seabreeze.robot.data.websocket.request.Request
import com.seabreeze.robot.data.websocket.request.SubscribeRequest
import io.netty.channel.*
import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.handler.codec.MessageToMessageEncoder
import io.netty.handler.codec.http.DefaultHttpHeaders
import io.netty.handler.codec.http.websocketx.*
import io.netty.handler.timeout.IdleStateEvent
import io.netty.handler.timeout.IdleStateHandler
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.util.concurrent.Future
import io.netty.util.concurrent.GenericFutureListener
import java.net.URI
import java.util.concurrent.TimeUnit


/**
 * User: milan
 * Time: 2022/2/11 18:26
 * Des: 编解码相关类资源
 */
/**
 * Netty 中 WebSocket 连接相关
 */
object WebSocketClientHandshakerSingle {

    var mHandshaker: WebSocketClientHandshaker? = null

    fun create(
        host: String,
        port: Int,
        channel: Channel,
        listener: GenericFutureListener<Future<in Void>>?
    ) {
        val create = URI.create("ws://$host:$port")
        val headers = DefaultHttpHeaders()
        headers.set("os-version", AppContext.getAppVersionName())
        headers.set("os-system", android.os.Build.VERSION.RELEASE)
        headers.set("os-model", android.os.Build.MODEL)
        mHandshaker = WebSocketClientHandshakerFactory.newHandshaker(
            create,
            WebSocketVersion.V13,
            null,
            true,
            headers,
            65536 * 10 * 1024
        )?.apply {
            handshake(channel).addListener(listener)
        }
    }

}

private var heartBeatInterval: Long = 5

/**
 * 心跳机制主要是用来检测远端是否存活
 */
class ClientIdleCheckHandler :
    IdleStateHandler(0, heartBeatInterval, 0, TimeUnit.SECONDS)

/**
 * 客户端业务心跳包 ChannelInboundHandlerAdapter
 */
@ChannelHandler.Sharable
class KeepAliveHandler : ChannelInboundHandlerAdapter() {

    override fun userEventTriggered(ctx: ChannelHandlerContext, evt: Any?) {
//        Logger.d("KeepAliveHandler userEventTriggered $evt")
        if (evt === IdleStateEvent.FIRST_WRITER_IDLE_STATE_EVENT) {
//            XLog.d("write idle happen. so need to send keepAlive to keep connection not closed by server")
            val requestModel = RequestModel<AlineOperation>(
                op = Request.RequestOp.ALINE,
                topic = "/keepAlive",
                msg = AlineOperation()
            )
            val responseMessage = requestModel.gToJson()
//            ReferenceCountUtil.retain(responseMessage)
            if (ctx.channel().isActive && ctx.channel().isWritable) {
                val textWebSocketFrame = TextWebSocketFrame(responseMessage)
                ctx.write(textWebSocketFrame)
            } else {
                Logger.e("not writable now, message dropped");
            }
        } else
            super.userEventTriggered(ctx, evt)
    }
}

//-------------------------- Encoder
/**
 * 登录验证，将 AuthOperation 编码为 Json
 */
class AuthEncoderHandler : MessageToMessageEncoder<AuthOperation>() {
    override fun encode(ctx: ChannelHandlerContext, msg: AuthOperation, out: MutableList<Any>) {
        Logger.e("AuthEncoderHandler $msg")
        val requestModel = RequestModel<AuthOperation>(
            op = Request.RequestOp.AUTH,
            topic = "/password",
            msg = msg
        )
        out.add(requestModel.gToJson())
    }
}//AuthEncoderHandler

/**
 * 将 Json String 编码为 TextWebSocketFrame
 */
class JsonStrToWebSocketFrameEncoderHandler : MessageToMessageEncoder<String>() {
    override fun encode(
        ctx: ChannelHandlerContext,
        responseMessage: String,
        out: MutableList<Any>
    ) {
//        Logger.d("StringToTextWebSocketFrameEncoderHandler $responseMessage")
        val textWebSocketFrame = TextWebSocketFrame(responseMessage)
        out.add(textWebSocketFrame)
    }
}//TextWebSocketEncoderHandler

/**
 * 将 TextWebSocketFrame 发送给服务器
 */
class WebSocketFrameWriteAndFlushEncoderHandler : MessageToMessageEncoder<TextWebSocketFrame>() {

    override fun encode(
        ctx: ChannelHandlerContext,
        msg: TextWebSocketFrame,
        out: MutableList<Any>?
    ) {
//        Logger.d("WebSocketFrameWriteAndFlushEncoderHandler $msg")
        if (ctx.channel().isActive && ctx.channel().isWritable) {
            ctx.writeAndFlush(msg)
        } else {
            Logger.e("not writable now, message dropped");
        }
    }
}//WebSocketFrameLastEncoderHandler


//-------------------------- Decoder
/**
 * 将 WebSocketFrame 解码为 String
 */
class WebSocketFrameToJsonStrDecoderHandler : MessageToMessageDecoder<WebSocketFrame>() {

    override fun decode(
        ctx: ChannelHandlerContext,
        webSocketFrame: WebSocketFrame,
        out: MutableList<Any>
    ) {
//        Logger.d("WebSocketFrameToJsonStrDecoderHandler $webSocketFrame")

        if (webSocketFrame is CloseWebSocketFrame) {
            Logger.e("WebSocketFrameToJsonStrDecoderHandler CloseWebSocketFrame close ... ")
            return
        }
        if (webSocketFrame is PingWebSocketFrame) {
            ctx.channel().write(PongWebSocketFrame(webSocketFrame.content().retain()));
            return
        }
        if (webSocketFrame !is TextWebSocketFrame) {
            Logger.e("本例程仅支持文本消息，不支持二进制消息")
            throw UnsupportedOperationException(
                String.format(
                    "%s frame types not supported",
                    webSocketFrame.javaClass.name
                )
            )
        }
        val text = webSocketFrame.text()
        out.add(text)
    }
}//WebSocketFrameDecoderHandler

/**
 * 将 SubscribeRequest 解码为 json Str
 */
class SubscribeRequestEncoderHandler : MessageToMessageEncoder<SubscribeRequest>() {
    override fun encode(
        ctx: ChannelHandlerContext?,
        subscribeRequest: SubscribeRequest,
        out: MutableList<Any>
    ) {
//        Logger.d("SubscribeRequestDecoderHandler $subscribeRequest")
        val requestMessage = subscribeRequest.getRequest()
        out.add(requestMessage)
    }
}//SubscribeRequestHandler

/**
 * 将 PDU 解码为 json Str
 */
class PDURequestEncoderHandler : MessageToMessageEncoder<PDU>() {
    override fun encode(
        ctx: ChannelHandlerContext?,
        pdu: PDU,
        out: MutableList<Any>
    ) {
        Logger.d("PDURequestDecoderHandler $pdu")
        val request = PublishRequest(
            pdu.pduMessage(StreamGenerator.getStreamId()),
        )
        val requestMessage = request.getRequest()
        out.add(requestMessage)
    }
}//PDURequestDecoderHandler

private var READER_IDLE_TIME_AUTH: Int = 5

/**
 * 认证超时处理
 */
class AuthReadTimeoutHandler(private val promise: ChannelPromise) :
    ReadTimeoutHandler(READER_IDLE_TIME_AUTH) {
    override fun readTimedOut(ctx: ChannelHandlerContext?) {
        super.readTimedOut(ctx)
        promise.setFailure(SocketOvertimeThrowable())
    }
}//AuthReadTimeoutHandler

private var READER_IDLE_TIME_JSON: Int = 3

/**
 * Json 延迟处理
 */
class JsonRequestTimeoutHandler(private val future: OperationResultFuture<*>) :
    ReadTimeoutHandler(READER_IDLE_TIME_JSON) {
    override fun readTimedOut(ctx: ChannelHandlerContext?) {
//        super.readTimedOut(ctx)
        Logger.e("readTimedOut")
        future.setFailure(SocketOvertimeThrowable())
    }
}//JsonRequestDecoderHandler