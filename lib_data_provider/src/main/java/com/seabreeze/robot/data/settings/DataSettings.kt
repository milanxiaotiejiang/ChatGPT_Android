package com.seabreeze.robot.data.settings

import com.seabreeze.robot.base.ext.foundation.Mmkv

/**
 * <pre>
 * author : 76515
 * time   : 2020/6/28
 * desc   :
 * </pre>
 */
object DataSettings {

    var open_ai_api: String by Mmkv("open_ai_api", "")
    var token_app: String by Mmkv("TOKEN_APP", "")

}