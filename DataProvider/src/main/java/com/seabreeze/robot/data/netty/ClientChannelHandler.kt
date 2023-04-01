package com.seabreeze.robot.data.netty

import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.handler.codec.http.HttpClientCodec
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.flush.FlushConsolidationHandler
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.InsecureTrustManagerFactory
import io.netty.handler.stream.ChunkedWriteHandler


/**
 * User: milan
 * Time: 2022/2/17 16:20
 * Des: 设置解码器、编码器
 * 用于在某个Channel注册到EventLoop后，对这个Channel执行一些初始化操作。
 * ChannelInitializer虽然会在一开始会被注册到Channel相关的pipeline里，但是在初始化完成之后，ChannelInitializer会将自己从pipeline中移除，不会影响后续的操作。
 */
class ClientChannelHandler(nettyThread: NettyThread) : ChannelInitializer<NioSocketChannel>() {

    private val reconnectHandler = ReconnectHandler(nettyThread)
    private val httpServerKeepAliveHandler = KeepAliveHandler()
    private val debugLogHandler = LoggingHandler(LogLevel.INFO)
    private val infoLogHandler = LoggingHandler(LogLevel.DEBUG)

    private var sslContext: SslContext
    var fullHttpResponseHandler: FullHttpResponseHandler
    var jsonBusinessDecoderHandler: JsonBusinessDecoderHandler

    init {
        val sslContextBuilder = SslContextBuilder.forClient()
        sslContextBuilder.trustManager(InsecureTrustManagerFactory.INSTANCE)
        sslContext = sslContextBuilder.build()
        fullHttpResponseHandler = FullHttpResponseHandler()
        jsonBusinessDecoderHandler = JsonBusinessDecoderHandler()
    }

    override fun initChannel(ch: NioSocketChannel) {

        val pipeline = ch.pipeline()

        pipeline.addLast(reconnectHandler)

//        pipeline.addLast("headLog", debugLogHandler)

        pipeline.addLast(ClientIdleCheckHandler())

//        pipeline.addLast(sslContext.newHandler(ch.alloc()))

        pipeline.addLast("http-codec", HttpClientCodec()) //设置解码器
        pipeline.addLast("aggregator", HttpObjectAggregator(65536 * 10 * 1024)) //聚合器，使用websocket会用到
        pipeline.addLast("http-chunked", ChunkedWriteHandler()) //用于大数据的分区传输

        pipeline.addLast("fullHttpResponse", fullHttpResponseHandler)

        pipeline.addLast(
            "webSocketFrameWriteAndFlushEncoder",
            WebSocketFrameWriteAndFlushEncoderHandler()
        )

        pipeline.addLast("webSocketFrameToJsonStrDecoder", WebSocketFrameToJsonStrDecoderHandler())
        pipeline.addLast("jsonStrToWebSocketFrameEncoder", JsonStrToWebSocketFrameEncoderHandler())

        pipeline.addLast("authEncoder", AuthEncoderHandler())

        pipeline.addLast("jsonBusinessDecoder", jsonBusinessDecoderHandler)
        pipeline.addLast("subscribeRequestEncoder", SubscribeRequestEncoderHandler())
        pipeline.addLast("pduRequestEncoder", PDURequestEncoderHandler())

        pipeline.addLast("infoLog", infoLogHandler)

        pipeline.addLast(httpServerKeepAliveHandler)

        pipeline.addLast("flushConsolidation", FlushConsolidationHandler(10, true))
    }

    fun setNeedReconnect(needReconnect: Boolean) {
        reconnectHandler.setNeedReconnect(needReconnect)
    }
}