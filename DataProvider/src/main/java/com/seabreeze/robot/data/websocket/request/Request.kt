package com.seabreeze.robot.data.websocket.request

import android.os.Looper
import androidx.annotation.IntDef
import androidx.annotation.StringDef
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.Platform
import com.seabreeze.robot.data.websocket.RosWebSocketManager
import com.seabreeze.robot.data.websocket.request.callback.IRequestCallback

/**
 * User: milan
 * Time: 2021/9/3 14:58
 * Des:
 */
abstract class Request(
    @RequestType val type: Int, val msg: String
) {

    private lateinit var request: String
    var mCallback: IRequestCallback? = null

    var attempts = 0
        private set

    fun increaseAttempts() {
        attempts++
    }

    var overtime: Long = RosWebSocketManager.DEFAULT_DELAY_FOR_REQUEST.toLong()

    fun getRequest(): String {
        if (!this::request.isInitialized) {
            request = transformToRequest()
        }

        return if (request.isEmpty()) msg
        else request
    }

    //        mMsg --> request
    abstract fun transformToRequest(): String

    fun resultBefore() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            Platform.get().execute {
                mCallback?.apply {
                    onBefore()
                }
            }
        } else {
            mCallback?.apply {
                onBefore()
            }
        }
    }

    fun resultSuccess() {
        if (Looper.getMainLooper() != Looper.myLooper()) {
            Platform.get().execute {
                mCallback?.apply {
                    onAfter()
                    onSuccess(msg)
                }
            }
        } else {
            mCallback?.apply {
                onAfter()
                onSuccess(msg)
            }
        }
    }

    fun resultFail(throwable: BaseThrowable) {
//        throwable.printStackTrace()
        if (Looper.getMainLooper() != Looper.myLooper()) {
            Platform.get().execute {
                mCallback?.apply {
                    onAfter()
                    onFail(throwable)
                }
            }
        } else {
            mCallback?.apply {
                onAfter()
                onFail(throwable)
            }
        }
    }

    @IntDef(flag = true, value = [RequestType.SUBSCRIBE, RequestType.PUBLISH, RequestType.ONE_WAY])
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class RequestType {
        companion object {
            const val SUBSCRIBE = 0 //订阅
            const val PUBLISH = 1 //需要返回值
            const val ONE_WAY = 2 //单发送
        }
    }

    /**
     * json 中的 op
     */
    @StringDef(value = [RequestOp.SUBSCRIBE, RequestOp.PUBLISH, RequestOp.AUTH, RequestOp.ALINE])
    @kotlin.annotation.Retention(AnnotationRetention.SOURCE)
    annotation class RequestOp {
        companion object {
            const val SUBSCRIBE = "subscribe"
            const val PUBLISH = "publish"
            const val AUTH = "auth"
            const val ALINE = "aline"
        }
    }

}