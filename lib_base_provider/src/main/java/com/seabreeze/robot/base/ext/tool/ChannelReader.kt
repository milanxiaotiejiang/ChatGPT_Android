package com.seabreeze.robot.base.ext.tool

import android.content.Context
import com.tencent.vasdolly.helper.ChannelReaderUtil

/**
 * User: milan
 * Time: 2022/6/23 13:48
 * Des:
 */
const val CHANNEL_DEBUG = "debug"
const val CHANNEL_DEV = "dev"
const val CHANNEL_UAT = "uat"
const val CHANNEL_PRE = "pre"
const val CHANNEL_PROD = "prod"

fun Context.appChannel() = ChannelReaderUtil.getChannel(this) ?: CHANNEL_DEBUG

fun Context.isDebugChannel() = appChannel() == CHANNEL_DEBUG
fun Context.isDevChannel() = appChannel() == CHANNEL_DEV
fun Context.isUatChannel() = appChannel() == CHANNEL_UAT
fun Context.isPreChannel() = appChannel() == CHANNEL_PRE
fun Context.isProdChannel() = appChannel() == CHANNEL_PROD
