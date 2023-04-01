package com.seabreeze.robot.data.netty

import com.orhanobut.logger.Logger
import io.netty.channel.*
import io.netty.handler.codec.http.FullHttpResponse

/**
 * User: milan
 * Time: 2022/2/18 12:46
 * Des: 建立 tcp 连接后的 webSocket 连接
 */
@ChannelHandler.Sharable
class FullHttpResponseHandler :
    SimpleChannelInboundHandler<FullHttpResponse>() {

    private var mChannelPromise: ChannelPromise? = null
    private var mChannelHandlerContext: ChannelHandlerContext? = null

    var onFullHttpStatusListener: ((fullHttpStatus: FullHttpStatus) -> Unit)? = null

    override fun channelRead0(
        ctx: ChannelHandlerContext,
        msg: FullHttpResponse
    ) {//一般是要重写 channelRead0() 这个方法的
        Logger.e("FullHttpRequestHandler WebSocket Info $msg")
        WebSocketClientHandshakerSingle.mHandshaker?.apply {
            if (!isHandshakeComplete) {
                finishHandshake(ctx.channel(), msg)
                onFullHttpStatusListener?.invoke(FullHttpStatus.FinishHandshake(ctx.channel()))
            }
        }
    }

    override fun channelActive(ctx: ChannelHandlerContext) {
        super.channelActive(ctx)
        mChannelHandlerContext = ctx
        onFullHttpStatusListener?.invoke(FullHttpStatus.Active(ctx.channel()))
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
//        super.channelInactive(ctx)
        mChannelHandlerContext = null
        onFullHttpStatusListener?.invoke(FullHttpStatus.Inactive(ctx.channel()))
    }

    override fun handlerAdded(ctx: ChannelHandlerContext) {
        super.handlerAdded(ctx)
        mChannelPromise = ctx.newPromise()
    }

    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        super.handlerRemoved(ctx)
        mChannelPromise = null
    }

}

sealed class FullHttpStatus {
    class Active(channel: Channel) : FullHttpStatus()
    class Inactive(channel: Channel) : FullHttpStatus()
    class FinishHandshake(val channel: Channel) : FullHttpStatus()
}