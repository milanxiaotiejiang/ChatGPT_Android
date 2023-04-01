package com.seabreeze.robot.data.netty

import android.util.Log
import com.elvishew.xlog.XLog
import com.orhanobut.logger.Logger
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.tool.currentTimeMillis
import com.seabreeze.robot.data.websocket.ChannelThrowable
import com.seabreeze.robot.data.websocket.DisconnectedThrowable
import com.seabreeze.robot.data.websocket.RosWebSocketManager
import com.seabreeze.robot.data.websocket.WsStatus
import com.seabreeze.robot.data.websocket.model.hex.base.PDU
import com.seabreeze.robot.data.websocket.model.json.base.RosRequest
import com.seabreeze.robot.data.websocket.request.JsonRequest
import com.seabreeze.robot.data.websocket.request.SubscribeRequest
import com.seabreeze.robot.data.websocket.request.callback.JsonRequestCallback
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.ChannelPipeline
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioChannelOption
import io.netty.channel.socket.nio.NioSocketChannel
import io.netty.util.concurrent.DefaultThreadFactory
import io.netty.util.concurrent.Future
import io.netty.util.concurrent.GenericFutureListener
import java.util.concurrent.atomic.AtomicBoolean


/**
 * User: milan
 * Time: 2022/2/21 13:52
 * Des:
 */
class NettyThread(
    address: RemoteAddress,
    private val needReconnect: Boolean = true,
    listener: CommunicationListener
) :
    CommunicationThread(address, listener) {

    private lateinit var mRemoteAddress: RemoteAddress
    private lateinit var mWorkGroup: NioEventLoopGroup
    private lateinit var mClientChannelHandler: ClientChannelHandler
    private lateinit var mBootstrap: Bootstrap
    private lateinit var mChannelFuture: ChannelFuture

    var atomicOpen = AtomicBoolean(false)

    var retryPolicy: RetryPolicy = ExponentialBackOffRetry()

    var mChannel: Channel? = null

    private val onConnectListener: GenericFutureListener<Future<in Void>> =
        GenericFutureListener {
            if (it.isSuccess) {
                Logger.d("ChannelFuture TCP connection successful.")
                val channel = mChannelFuture.channel()
                if (channel != null && channel.isActive && channel.isOpen) {
                    Logger.d("ChannelFuture channel isOpen & isActive.")
                    mChannel = channel
                    WebSocketClientHandshakerSingle.create(
                        mRemoteAddress.inetHost,
                        mRemoteAddress.inetPort,
                        channel,
                        onWebSocketClientHandshakerListener
                    )
                } else {
                    connectFail(ChannelThrowable("channel error ${channel.isActive} ${channel.isOpen}"))
                }
            } else {
                if (ConnectStatus.getCurrentStatus() is WsStatus.Connect.DisConnected
                    || ConnectStatus.getConnectingStatus() == WsStatus.Connecting.CONNECTING
                ) {
                    connectFail(BaseThrowable.ExternalThrowable(it.cause()))
                } else {
                    Logger.e("ChannelFuture TCP connection failed. " + ConnectStatus.getCurrentStatus())
                }
            }
        }

    private val onWebSocketClientHandshakerListener: GenericFutureListener<Future<in Void>> =
        GenericFutureListener {
            if (it.isSuccess) {
                Logger.d("webSocket handshaker successful.")
            } else {
                Logger.e("webSocket handshaker failed.")
                connectFail(BaseThrowable.ExternalThrowable(it.cause()))
            }
        }

    private val onFullHttpStatusListener: ((fullHttpStatus: FullHttpStatus) -> Unit) =
        { fullHttpStatus ->
            when (fullHttpStatus) {
                is FullHttpStatus.Active -> {
                    Logger.d("FullHttpStatus Active")
                }
                is FullHttpStatus.Inactive -> {
                    Logger.d("FullHttpStatus Inactive")
                }
                is FullHttpStatus.FinishHandshake -> {
                    Logger.d("FullHttpStatus FinishHandshake")
                    mClientChannelHandler.setNeedReconnect(true)
                    if (RosWebSocketManager.INSTANCE.isAuth) {
                        sendAuthMsg()
                    } else {
                        ConnectStatus.setConnected()
                        onConnectComplete()
                    }
                }
            }
        }

    override fun execute(remoteAddress: RemoteAddress) {
        mRemoteAddress = remoteAddress
        configureAndConnectServer()
    }

    private fun configureAndConnectServer() {
        mWorkGroup = NioEventLoopGroup(0, DefaultThreadFactory("netty-work"))

        mClientChannelHandler = ClientChannelHandler(this)
        mClientChannelHandler.fullHttpResponseHandler
            .onFullHttpStatusListener = onFullHttpStatusListener
        mClientChannelHandler.setNeedReconnect(needReconnect)

        mBootstrap = Bootstrap()
            .channel(NioSocketChannel::class.java)
            .option(NioChannelOption.TCP_NODELAY, true)//屏蔽Nagle算法试图
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .remoteAddress(mRemoteAddress.inetHost, mRemoteAddress.inetPort)
            .group(mWorkGroup)
            .handler(mClientChannelHandler)

        connectServer()
    }

    private fun connectServer() {
        try {
            atomicOpen.set(true)
            mChannelFuture = mBootstrap.connect()
            mChannelFuture.addListener(onConnectListener).sync()
            // Wait until the connection is closed.
            mChannelFuture.channel()?.closeFuture()?.sync()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun reconnect() {
        connectServer()
    }

    fun foreclosure() {
        connectFail(DisconnectedThrowable())
    }

    private fun sendAuthMsg(
        name: String = NettyImpl.TEST_NAME,
        pass: String = NettyImpl.TEST_PASS
    ) {
        mChannel?.run {
            val authOperation = AuthOperation(StreamGenerator.getStreamId(), name, pass)
            val newPromise = pipeline().newPromise()
            val timeOutHandler = AuthReadTimeoutHandler(newPromise)
            pipeline().addFirst(timeOutHandler)
            newPromise.addListener {
                pipeline().remove(timeOutHandler)
                if (it.isSuccess) {
                    ConnectStatus.setConnected()
                    onConnectComplete()
                } else {
                    connectFail(BaseThrowable.ExternalThrowable(it.cause()))
                }
            }
            mClientChannelHandler.jsonBusinessDecoderHandler.mAuthPromise = newPromise
            write(authOperation)
        }
    }

    private fun connectFail(throwable: BaseThrowable) {
        onConnectFailed(throwable)
        disconnect()
    }

    fun disconnect() {
        atomicOpen.set(false)
        RequestFuturePool.removeAll()
        if (this::mClientChannelHandler.isInitialized)
            mClientChannelHandler.setNeedReconnect(false)
        if (this::mChannelFuture.isInitialized) {
            mChannelFuture.run {
                if (channel() != null && channel().isOpen) {
                    channel().close()
                    channel().disconnect()
                }
            }
        }
        if (this::mWorkGroup.isInitialized) {
            mWorkGroup.shutdownGracefully()
        }
        ConnectStatus.setDisConnected()
    }

    fun isConnected(): Boolean {
        if (ConnectStatus.getCurrentStatus() is WsStatus.Connect.Connected) {
            if (mChannel != null) {
                if (mChannel!!.isOpen && mChannel!!.isActive) {
                    return true
                }
            }
        }
        return false
    }

    fun sendSubscribeMsg(topic: String) = mChannel?.writeAndFlush(SubscribeRequest(topic))

    fun sendOneWayMsg(pdu: PDU) = mChannel?.writeAndFlush(pdu)

}

const val JSON_REQUEST_TIMEOUT_HANDLER = "jsonRequestTimeoutHandler"

inline fun <T, reified R> NettyThread.sendJsonMsg(
    rosRequest: RosRequest<T>,
    overtime: Long = RosWebSocketManager.DEFAULT_DELAY_FOR_REQUEST.toLong(),
    callback: JsonRequestCallback<R>
) {
    val streamId = StreamGenerator.getStreamId()
    rosRequest.id = streamId

    val request = JsonRequest<T, R>(rosRequest)

    request.mCallback = callback
    request.setTypeToken<R>()
    request.overtime = overtime

    request.resultBefore()

    mChannel?.run {
        val start = currentTimeMillis
        Log.e(
            "NettyThread",
            " ──────────────────────────────────────────────────────── Start $streamId ──────────────────────────────────────────────────────── "
        )
        val future = RequestFuturePool.add(streamId, request)
        val realRequest = request.getRequest()
        XLog.e("NettyThread request: $realRequest")
        write(realRequest)
//        pipeline().removeTimeoutHandler()
//        pipelin e().addFirst(JSON_REQUEST_TIMEOUT_HANDLER, JsonRequestTimeoutHandler(future))
        future.addListener {
//            pipeline().removeTimeoutHandler()
            if (it.isSuccess) {
                val now = it.now
                if (now is R) {
                    XLog.e("NettyThread response params: $now")
                    request.resultJsonSuccess(now)
                }
            } else {
                RequestFuturePool.remove(streamId)
                val cause = it.cause()
                if (cause is BaseThrowable.InsideThrowable) {
                    request.resultFail(cause)
                } else {
                    request.resultFail(BaseThrowable.ExternalThrowable(cause))
                }
            }

            Log.e(
                "NettyThread",
                " ──────────────────────────────────────────────────────── End $streamId ${it.isSuccess} ${currentTimeMillis - start} ms ──────────────────────────────────────────────────────── "
            )
        }
    }

}

fun ChannelPipeline.removeTimeoutHandler() {
    if (context(JSON_REQUEST_TIMEOUT_HANDLER) != null) {
        remove(JSON_REQUEST_TIMEOUT_HANDLER)
    }
}