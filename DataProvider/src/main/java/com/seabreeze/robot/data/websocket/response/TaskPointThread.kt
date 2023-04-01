package com.seabreeze.robot.data.websocket.response

import android.os.Handler
import com.seabreeze.robot.data.websocket.model.json.RosTaskPoint
import java.io.IOException
import java.util.concurrent.LinkedBlockingQueue

/**
 * User: milan
 * Time: 2021/10/13 20:13
 * Des:
 */
class TaskPointThread(handler: Handler) : Thread() {

    private val mLinkedBlockingQueue = LinkedBlockingQueue<RosTaskPoint>()

    init {
        name = "$NAME $id"
    }

    @Volatile
    private var isRunning = false

    override fun run() {
        super.run()

        listenStream()
        endConnection()
    }

    private fun listenStream() {
        isRunning = true
        while (isRunning) {
            try {
                val take = mLinkedBlockingQueue.take()
                take?.apply {

                }
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun endConnection() {
        isRunning = false
        interrupt()
    }

    fun sendData(point: RosTaskPoint) {
        mLinkedBlockingQueue.add(point)
    }

    fun cancel() {
        if (isRunning) {
            endConnection()
        }
    }

    companion object {
        private const val NAME = "Task-Point-Thread"
    }

}