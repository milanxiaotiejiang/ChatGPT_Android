package com.seabreeze.robot.base.router

import android.content.Context
import android.content.Intent
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.alibaba.android.arouter.launcher.ARouter
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.router.RouterPath.AppCenter.*
import com.seabreeze.robot.base.router.RouterPath.UserCenter.*


/**
 * User: milan
 * Time: 2020/4/8 13:39
 * Des:
 */
const val MAIN_TOKEN_INVALID = "main_token_invalid"
const val MAIN_TCP_CONNECT = "main_tcp_connect"

fun startMain(tokenInvalid: Boolean = false, isConnect: Boolean = false) {
    ARouter.getInstance()
        .build(PATH_APP_MAIN)
        .withBoolean(MAIN_TOKEN_INVALID, tokenInvalid)
        .withBoolean(MAIN_TCP_CONNECT, isConnect)
        .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        .withTransition(R.anim.fade_out, R.anim.fade_in)
        .navigation()
}

const val MESSAGE_TITLE = "message_title"
const val MESSAGE_CONTENT = "message_content"
const val MESSAGE_FROM = "message_from"
fun startMessage(
    messageTitle: String = "",
    messageContent: String = "",
    messageFrom: Boolean = false
) {
    ARouter.getInstance()
        .build(PATH_APP_MESSAGE)
        .withString(MESSAGE_TITLE, messageTitle)
        .withString(MESSAGE_CONTENT, messageContent)
        .withBoolean(MESSAGE_FROM, messageFrom)
        .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        .withTransition(R.anim.fade_out, R.anim.fade_in)
        .navigation()
}

fun startLogin(enterAnim: Int = R.anim.fade_out, exitAnim: Int = R.anim.fade_in) {
    ARouter.getInstance()
        .build(PATH_APP_LOGIN)
        .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        .withTransition(enterAnim, exitAnim)
        .navigation()
}

fun startLoginNebula(enterAnim: Int = R.anim.fade_out, exitAnim: Int = R.anim.fade_in) {
    ARouter.getInstance()
        .build(PATH_APP_LOGIN_NEW)
        .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        .withTransition(enterAnim, exitAnim)
        .navigation()
}

fun startAdvertisement() {
    ARouter.getInstance()
        .build(PATH_APP_ADVERTISEMENT)
        .withFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        .withTransition(R.anim.fade_out, R.anim.fade_in)
        .navigation()
}

const val PROTOCOL_ENUM = "protocol_enum"
fun startProtocol(anEnum: String) {
    ARouter.getInstance()
        .build(PATH_APP_PROTOCOL)
        .withString(PROTOCOL_ENUM, anEnum)
        .withTransition(R.anim.fade_out, R.anim.fade_in)
        .navigation()
}

const val WEB_URL = "WEB_URL"
fun startWeb(url: String) {
    ARouter.getInstance()
        .build(PATH_APP_WEB)
        .withString(WEB_URL, url)
        .withTransition(R.anim.fade_out, R.anim.fade_in)
        .navigation()
}

fun startShare() {
    ARouter.getInstance()
        .build(PATH_APP_SHARE)
        .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        .withTransition(R.anim.fade_out, R.anim.fade_in)
        .navigation()
}

fun startPay() {
    ARouter.getInstance()
        .build(PATH_APP_PAY)
        .withFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        .withTransition(R.anim.fade_out, R.anim.fade_in)
        .navigation()
}


@Interceptor(priority = 8)
class TestInterceptor : IInterceptor {
    override fun process(postcard: Postcard, callback: InterceptorCallback) {
//        XLog.e(postcard)
        callback.onContinue(postcard)
    }

    override fun init(context: Context?) {
    }
}