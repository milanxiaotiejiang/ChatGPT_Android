@file:JvmName("ConvertUtil")

package com.seabreeze.robot.base.ext.tool

import java.nio.ByteBuffer

/**
 * User: milan
 * Time: 2021/9/3 19:33
 * Des:
 */
fun Int.intToByteArray4(): ByteArray {
    val byteArray = ByteArray(4)
    byteArray[0] = ((this shr 24) and 0xff).toByte()
    byteArray[1] = ((this shr 16) and 0xff).toByte()
    byteArray[2] = ((this shr 8) and 0xff).toByte()
    byteArray[3] = (this and 0xff).toByte()
    return byteArray
}

private fun intToByteArray2(num: Int): ByteArray {
    val byteArray = ByteArray(2)
    byteArray[0] = ((num shr 8) and 0xff).toByte()
    byteArray[1] = (num and 0xff).toByte()
    return byteArray
}

fun ByteArray.byteArrayToInt() =
    ByteBuffer.wrap(this).int
