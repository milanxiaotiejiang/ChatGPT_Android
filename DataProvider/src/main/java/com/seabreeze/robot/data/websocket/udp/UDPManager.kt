package com.seabreeze.robot.data.websocket.udp

import android.content.Context
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.MulticastLock
import com.seabreeze.robot.base.ext.coroutine.launchFlow
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.util.concurrent.atomic.AtomicBoolean


/**
 * User: milan
 * Time: 2021/9/22 14:06
 * Des:
 */
object UDPManager {

    private const val DEFAULT_UDP_PORT = 9099

    private var lock: MulticastLock? = null
    private var scope: Job? = null
    private var listenSocket: DatagramSocket? = null

    private val replyBuf = ByteArray(1024)

    private var flag: AtomicBoolean = AtomicBoolean(false)

    fun start(context: Context, success: (UDPData) -> Unit, fail: (BaseThrowable) -> Unit) {
        if (flag.get()) {
            return
        }

        end()

        val manager = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
        lock = manager.createMulticastLock("UDPManager").apply {
            setReferenceCounted(true)
        }

        scope = GlobalScope.launch(Dispatchers.Main) {
            launchFlow {

                try {
                    val receiverPacket = DatagramPacket(replyBuf, replyBuf.size)

                    listenSocket = DatagramSocket(DEFAULT_UDP_PORT)
                        .apply {
                            broadcast = true
                            soTimeout = 5000
                            reuseAddress = true
                        }

                    lock?.acquire()
                    listenSocket?.receive(receiverPacket)

                    if (receiverPacket.address != null) {
                        return@launchFlow UDPData(
                            receiverPacket.address.hostAddress,
                            String(receiverPacket.data, 0, receiverPacket.length)
                        )
                    } else {
                        throw IllegalAccessException("address is null")
                    }
                } catch (e: Exception) {
                    throw e
                } finally {
                    lock?.apply {
                        if (isHeld)
                            release()
                    }
                    listenSocket?.close()
                    listenSocket = null
                }

            }
                .flowOn(Dispatchers.IO)
                .onStart {
                    flag.set(true)
                }
                .catch { e ->
                    fail(BaseThrowable.ExternalThrowable(e))
                    end()
                    flag.set(false)
                }
                .collect {
                    success(it)
                    end()
                    flag.set(false)
                }
        }
    }

    fun end() {
        lock?.apply {
            if (isHeld)
                release()
        }

        scope?.cancel()
        scope = null
    }
}

data class UDPData(
    val ip: String,
    val data: String
)