package com.seabreeze.robot.data.websocket.request.callback

import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.onError

/**
 * User: milan
 * Time: 2021/11/18 16:22
 * Des:
 */
interface IRequestCallback {
    fun onBefore()
    fun onAfter()
    fun onSuccess(success: String)
    fun onFail(throwable: BaseThrowable)
}

interface IJsonRequestCallback<Result> : IRequestCallback {
    fun onJsonSuccess(result: Result?)
}

open class RequestCallback : IRequestCallback {

    var onBeforeMethod: (() -> Unit)? = null
    var onAfterMethod: (() -> Unit)? = null
    var onSuccessMethod: ((String) -> Unit)? = null
    var onFailMethod: (BaseThrowable) -> Unit = {
        it.onError()
    }

    override fun onBefore() {
        onBeforeMethod?.invoke()
    }

    override fun onAfter() {
        onAfterMethod?.invoke()
    }

    override fun onSuccess(success: String) {
        onSuccessMethod?.invoke(success)
    }

    override fun onFail(throwable: BaseThrowable) {
        onFailMethod.invoke(throwable)
    }
}

abstract class JsonRequestCallback<Result> : RequestCallback(), IJsonRequestCallback<Result>