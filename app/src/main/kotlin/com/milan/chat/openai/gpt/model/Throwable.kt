package com.milan.chat.openai.gpt.model

import android.app.Activity
import com.milan.chat.openai.gpt.ext.toast
import com.tapadoo.alerter.Alerter

/**
 * User: milan
 * Time: 2020/4/20 19:25
 * Des:
 */

sealed class BaseThrowable(
    val code: Int = -1, message: String? = null, cause: Throwable? = null
) : Throwable(message, cause) {

    open class ExternalThrowable(errorMessage: String?, throwable: Throwable) :
        BaseThrowable(message = errorMessage, cause = throwable) {

        constructor(throwable: Throwable) : this(throwable.message, throwable)
    }

    open class InsideThrowable(
        val errorCode: Int, val errorMessage: String
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
            }
        }
        isInside() -> {
            val insideThrowable = this as BaseThrowable.InsideThrowable
            toast { "${insideThrowable.errorCode} ${insideThrowable.errorMessage}" }
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