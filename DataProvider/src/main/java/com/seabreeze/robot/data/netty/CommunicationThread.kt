package com.seabreeze.robot.data.netty

import android.os.Handler
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import java.util.concurrent.locks.Lock
import java.util.concurrent.locks.ReentrantLock

/**
 * User: milan
 * Time: 2021/10/13 20:13
 * Des:
 */
abstract class CommunicationThread(
    private val address: RemoteAddress,
    listener: CommunicationListener
) : Thread() {

    lateinit var mCompleteListener: CommunicationCompleteListener
    lateinit var mErrorListener: CommunicationErrorListener

    private val mListenerHandler: Handler

    init {
        name = "$NAME $id"
        mListenerHandler = CommunicationListenerHandler(listener)
    }

    @Volatile
    private var isRunning = false

    override fun run() {
        super.run()

        listenStream()
        endConnection()
    }

    private val mLock: Lock = ReentrantLock()

    private fun listenStream() {
        isRunning = true
        sendMessage(CommunicationListenerHandler.Messages.READY)

        realConnect(address)
    }

    private fun realConnect(remoteAddress: RemoteAddress): Boolean {
        try {
            mLock.lockInterruptibly()
            try {
                execute(remoteAddress)
                return true
            } finally {
                mLock.unlock()
            }
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return false
    }

    abstract fun execute(remoteAddress: RemoteAddress)

    private fun endConnection() {
        isRunning = false
        sendMessage(CommunicationListenerHandler.Messages.ENDED)
        interrupt()
    }

    private fun sendMessage(@CommunicationListenerHandler.Messages message: Int) {
        mListenerHandler.obtainMessage(message, this).sendToTarget()
    }

    fun onConnectComplete() {
        if (this@CommunicationThread::mCompleteListener.isInitialized)
            mCompleteListener.onCommunicationComplete(address.inetHost)
    }

    fun onConnectFailed(throwable: BaseThrowable) {
        if (this::mErrorListener.isInitialized)
            mErrorListener.onCommunicationFailed(throwable)
    }

    companion object {
        private const val NAME = "Netty-Client-Thread"
    }

}

data class RemoteAddress(val inetHost: String, val inetPort: Int)