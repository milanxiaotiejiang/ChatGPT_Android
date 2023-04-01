package com.seabreeze.robot.base.ext.foundation

import android.app.Activity
import com.seabreeze.robot.base.ext.tool.postEvent
import com.seabreeze.robot.base.ext.tool.toast
import com.seabreeze.robot.base.model.TokenInvalidEvent
import com.seabreeze.robot.base.router.startMain
import com.tapadoo.alerter.Alerter
import retrofit2.HttpException

/**
 * User: milan
 * Time: 2020/4/20 19:25
 * Des:
 */

sealed class BaseThrowable(
    val code: Int = -1,
    message: String? = null,
    cause: Throwable? = null
) :
    Throwable(message, cause) {

    open class ExternalThrowable(errorMessage: String?, throwable: Throwable) :
        BaseThrowable(message = errorMessage, cause = throwable) {

        constructor(throwable: Throwable) : this(throwable.message, throwable)
    }

    open class InsideThrowable(
        val errorCode: Int,
        val errorMessage: String
    ) : BaseThrowable(code = errorCode, message = errorMessage)

    fun isExternal() = this is ExternalThrowable
    fun isInside() = this is InsideThrowable
}

fun BaseThrowable.onError() {
    when {
        isExternal() -> {
            val externalThrowable = this as BaseThrowable.ExternalThrowable
            externalThrowable.cause?.apply {
                message?.apply {
                    toast { this }
                }
                if (this is HttpException) {
                    when (code()) {
                        401 -> {
                            startMain(true)
                            postEvent(TokenInvalidEvent())
                        }
                    }
                }
            }
        }
        isInside() -> {
            val insideThrowable = this as BaseThrowable.InsideThrowable
            toast { insideThrowable.errorMessage }
            if (insideThrowable.errorCode == 401) {
                startMain(true)
                postEvent(TokenInvalidEvent())
            }
        }
    }
}

fun BaseThrowable.onError(activity: Activity) {
    when {
        isExternal() -> {
            val externalThrowable = this as BaseThrowable.ExternalThrowable
            externalThrowable.cause?.apply {
                message?.apply {
                    Alerter.create(activity).setText(this).show()
                }
            }
        }
        isInside() -> {
            val insideThrowable = this as BaseThrowable.InsideThrowable
            Alerter.create(activity)
                .setText("${insideThrowable.errorCode} ${insideThrowable.errorMessage}")
                .show()
        }
    }
}