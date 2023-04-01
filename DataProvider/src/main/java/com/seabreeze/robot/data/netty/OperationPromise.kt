package com.seabreeze.robot.data.netty

import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.otherwise
import com.seabreeze.robot.base.ext.foundation.yes
import com.seabreeze.robot.base.ext.tool.GsonExt
import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.data.R
import com.seabreeze.robot.data.websocket.AnalysisThrowable
import com.seabreeze.robot.data.websocket.SocketOvertimeThrowable
import com.seabreeze.robot.data.websocket.model.json.base.RosResponse
import com.seabreeze.robot.data.websocket.request.JsonRequest
import io.netty.util.HashedWheelTimer
import io.netty.util.TimerTask
import io.netty.util.concurrent.DefaultPromise
import io.netty.util.concurrent.GlobalEventExecutor
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit

/**
 * User: milan
 * Time: 2022/2/21 17:19
 * Des:
 */

class OperationResultFuture<V>(private val request: JsonRequest<*, V>) :
    DefaultPromise<V>(GlobalEventExecutor.INSTANCE) {

    private val mHashedWheelTimer = HashedWheelTimer(1, TimeUnit.SECONDS, 100)

    private val mTimerTask: TimerTask = TimerTask {
        setFailure(SocketOvertimeThrowable())
    }

    init {
        mHashedWheelTimer.newTimeout(mTimerTask, request.overtime, TimeUnit.MILLISECONDS)
    }

    fun analysis(simpleResponse: RosResponse<Any>, jsonStr: String) {
        mHashedWheelTimer.stop()
        simpleResponse.result.yes {
            val realResponse = GsonExt.gson.fromJson<RosResponse<V>>(jsonStr, request.mRosModeType)
            realResponse.run {
                result.yes {
                    setSuccess(params)
                }.otherwise {
                    setFailure(BaseThrowable.InsideThrowable(errorCode, errorMessage))
                }
            } ?: setFailure(AnalysisThrowable())
        }.otherwise {
            setFailure(
                BaseThrowable.InsideThrowable(
                    simpleResponse.errorCode,
                    simpleResponse.errorMessage
                )
            )
        }
    }

    fun wheelStop() {
        mHashedWheelTimer.stop()
        request.mCallback = null
    }
}

object RequestFuturePool {

    private var futureMap = ConcurrentHashMap<Long, OperationResultFuture<*>>()

    fun <R> add(key: Long, request: JsonRequest<*, R>): OperationResultFuture<R> {
        val future = OperationResultFuture(request)
        futureMap[key] = future
        return future
    }

    fun remove(streamId: Long) {
        futureMap.remove(streamId)
    }

    fun setResultInResponseJSON(dataStr: String) {
        val rosResponse = dataStr.gToBean<RosResponse<Any>>()
        rosResponse?.apply {
            try {
                val future = futureMap[id]
                future?.analysis(this, dataStr)
            } finally {
                futureMap.remove(id)
            }
        }
    }

    fun removeAll() {
        futureMap.forEach { (_, u) ->
            u.wheelStop()
        }
    }

}