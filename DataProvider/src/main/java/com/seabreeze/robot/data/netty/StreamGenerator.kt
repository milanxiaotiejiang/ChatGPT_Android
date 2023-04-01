package com.seabreeze.robot.data.netty

import java.util.concurrent.atomic.AtomicLong

/**
 * User: milan
 * Time: 2022/2/21 17:28
 * Des:
 *
 * IDX.incrementAndGet()
 */
object StreamGenerator {
    private val IDX = AtomicLong()
    fun getStreamId() = IDX.incrementAndGet()
}