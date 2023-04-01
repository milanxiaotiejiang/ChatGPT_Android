package com.milan.chat.openai.gpt

import android.content.Context
import com.elvishew.xlog.XLog
import com.milan.chat.openai.gpt.ui.chat.ChatMessagesActivity
import com.seabreeze.robot.base.common.CommonHelper
import com.seabreeze.robot.data.DataApplication

/**
 * User: milan
 * Time: 2023/3/30 20:02
 * Des:
 */
class App : DataApplication() {
    override fun attachBaseContext(base: Context) {
        CommonHelper.context = base
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onTerminate() {
        super.onTerminate()
        XLog.e("onTerminate !!!")
    }

    override fun buglyKey() = "b2f65156eb"

    override fun canShowUpgradeAct() = ChatMessagesActivity::class.java
}