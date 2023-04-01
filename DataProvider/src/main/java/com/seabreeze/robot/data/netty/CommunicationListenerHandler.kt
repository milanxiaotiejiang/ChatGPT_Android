package com.seabreeze.robot.data.netty

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.elvishew.xlog.XLog
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import java.lang.ref.WeakReference

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/8/4
</pre> *
 */
enum class CommunicationError {
    IO_EXCEPTION, INTERRUPTED_EXCEPTION
}

interface CommunicationListener {
    fun onCommunicationReady(thread: CommunicationThread) {}

    fun onCommunicationEnded(thread: CommunicationThread) {}

    fun onCommunicationFailed(
        thread: CommunicationThread,
        error: CommunicationError
    ) {
        XLog.e("onCommunicationFailed $error")
    }
}

interface CommunicationErrorListener {
    fun onCommunicationFailed(throwable: BaseThrowable)
}

interface CommunicationCompleteListener {
    fun onCommunicationComplete(host: String)
}

class CommunicationListenerHandler(listener: CommunicationListener) :
    Handler(Looper.getMainLooper()) {
    private val mReference: WeakReference<CommunicationListener> = WeakReference(listener)

    @Retention(AnnotationRetention.SOURCE)
    annotation class Messages {
        companion object {
            var READY = 1//准备
            var ENDED = -1//结束
            var IO_EXCEPTION = -2//io
            var INTERRUPTED_EXCEPTION = -3//中断
        }
    }

    override fun handleMessage(msg: Message) {
        super.handleMessage(msg)
        val listener = mReference.get()
        if (listener != null) {
            @Messages val what = msg.what
            val thread = msg.obj as CommunicationThread
            when (what) {
                Messages.READY -> listener.onCommunicationReady(thread)
                Messages.ENDED -> listener.onCommunicationEnded(thread)
                Messages.IO_EXCEPTION -> listener.onCommunicationFailed(
                    thread,
                    CommunicationError.IO_EXCEPTION
                )
                Messages.INTERRUPTED_EXCEPTION -> listener.onCommunicationFailed(
                    thread,
                    CommunicationError.INTERRUPTED_EXCEPTION
                )
            }
        }
    }

}