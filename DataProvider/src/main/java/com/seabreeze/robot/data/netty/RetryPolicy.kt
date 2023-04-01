package com.seabreeze.robot.data.netty

import com.elvishew.xlog.XLog
import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.math.max


/**
 * User: milan
 * Time: 2022/6/20 16:33
 * Des:
 */
interface RetryPolicy {
    fun allowRetry(retryCount: Int): Boolean
    fun getSleepTimeMs(retryCount: Int): Long
}

class ExponentialBackOffRetry(
    private val maxRetries: Int = MAX_RETRIES_LIMIT,
    private val baseSleepTimeMs: Long = 2000L,
    private val maxSleepMs: Long = 60 * 1000L
) : RetryPolicy {

    private val random: Random = Random()

    override fun allowRetry(retryCount: Int): Boolean {
        return retryCount < maxRetries
    }

    override fun getSleepTimeMs(retryCount: Int): Long {
        require(retryCount >= 0) { "retries count must greater than 0." }

        var computeRetryCount = retryCount

        if (computeRetryCount > MAX_RETRIES_LIMIT) {
            XLog.e("maxRetries too large ($maxRetries). Pinning to $MAX_RETRIES_LIMIT")
            computeRetryCount = MAX_RETRIES_LIMIT
        }

        var sleepMs = baseSleepTimeMs * max(1, random.nextInt(1 shl computeRetryCount))
        if (sleepMs > maxSleepMs) {
            XLog.e("Sleep extension too large ($sleepMs). Pinning to $maxSleepMs")
            sleepMs = maxSleepMs
        }
        return sleepMs
    }

    companion object {
        private const val MAX_RETRIES_LIMIT = 8
    }

}

@ChannelHandler.Sharable
class ReconnectHandler(private val nettyThread: NettyThread) : ChannelDuplexHandler() {

    private var retries = 0

    override fun channelActive(ctx: ChannelHandlerContext) {
        retries = 0
        super.channelActive(ctx)
    }

    override fun channelInactive(ctx: ChannelHandlerContext) {
        if (retries == 0)
            ctx.close()
        reconnect(ctx)
        ctx.fireChannelInactive()
    }

    override fun close(ctx: ChannelHandlerContext, promise: ChannelPromise?) {
        reconnect(ctx)
    }

    private fun reconnect(ctx: ChannelHandlerContext) {
        val allowRetry = nettyThread.retryPolicy.allowRetry(retries)
        if (isNeedReconnect && allowRetry) {
            val sleepTimeMs = nettyThread.retryPolicy.getSleepTimeMs(retries)
            XLog.e("Try to reconnect to the server after $sleepTimeMs ms. Retry count: ${++retries}.")
            ConnectStatus.setReconnecting()
            ctx.channel().eventLoop().schedule({
                XLog.e("Reconnecting ...")
                ConnectStatus.setReconnect()
                nettyThread.reconnect()
            }, sleepTimeMs, TimeUnit.MILLISECONDS)
        }
        if (!allowRetry && nettyThread.atomicOpen.get()) {
            XLog.e("Foreclosure ...")
            nettyThread.foreclosure()
        }
    }

    @Volatile
    private var isNeedReconnect = true

    fun setNeedReconnect(needReconnect: Boolean) {
        isNeedReconnect = needReconnect
    }

}
