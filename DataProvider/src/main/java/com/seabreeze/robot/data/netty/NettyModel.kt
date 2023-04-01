package com.seabreeze.robot.data.netty

import kotlin.random.Random


/**
 * User: milan
 * Time: 2022/2/11 18:05
 * Des:
 */
data class AuthOperation(
    val streamId: Long,
    val userName: String,
    val password: String,
)

data class AlineOperation(val random: Long = Random(Long.MAX_VALUE).nextLong())