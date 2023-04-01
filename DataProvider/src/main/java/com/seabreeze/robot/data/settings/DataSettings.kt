package com.seabreeze.robot.data.settings

import com.seabreeze.robot.base.ext.foundation.Mmkv
import com.seabreeze.robot.base.ext.tool.gToBean
import com.seabreeze.robot.base.ext.tool.gToJson
import com.seabreeze.robot.data.websocket.model.json.base.RosMap

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/28
 * desc   :
 * </pre>
 */
object DataSettings {

    var token_app: String by Mmkv("TOKEN_APP", "")

    var robot_ip: String by Mmkv("ROBOT_IP", "192.168.8.100")
    var robot_port: Int by Mmkv("ROBOT_PORT", 9090)
    var robot_login_check: Boolean by Mmkv("ROBOT_LOGIN_CHECK", false)

    var machine_model: String by Mmkv("MACHINE_MODEL", "600")

    var open_smart_partition: Boolean by Mmkv("OPEN_SMART_PARTITION", false)

    //**************************************************************************************************/
    private var current_account: String by Mmkv("CURRENT_ACCOUNT", "")

//    fun saveAccount(userInfo: UserInfo) {
//        current_account = userInfo.gToJson()
//    }
//
//    fun localAccount(): UserInfo? {
//        var info: UserInfo? = null
//        if (current_account.isNotEmpty()) {
//            info = current_account.gToBean<UserInfo>()
//        }
//        return info
//    }

    fun clear() {
        current_account = ""
    }

    //**************************************************************************************************/
    private var current_map: String by Mmkv("CURRENT_MAP", "")

    fun saveMap(map: RosMap) {
        current_map = map.gToJson()
    }

    fun localMap(): RosMap? {
        var map: RosMap? = null
        if (current_map.isNotEmpty()) {
            map = current_map.gToBean<RosMap>()
        }
        return map
    }
}