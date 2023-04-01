package com.seabreeze.robot.data.websocket.response

/**
 * User: milan
 * Time: 2022/2/21 15:22
 * Des:
 */
class DataBackupCenter {

    var msgCheckApp: String = ""

    companion object {

        val INSTANCE: DataBackupCenter by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            DataBackupCenter()
        }
    }
}