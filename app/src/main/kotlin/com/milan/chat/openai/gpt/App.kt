package com.milan.chat.openai.gpt

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.multidex.MultiDexApplication
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter

/**
 * User: milan
 * Time: 2023/3/30 20:02
 * Des:
 */
lateinit var INSTANCE: Application

class App : MultiDexApplication() {
    override fun attachBaseContext(base: Context) {
        CommonHelper.context = base
        super.attachBaseContext(base)
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        //XLog
        val config = LogConfiguration.Builder()
            .tag("Base_XLog")
            .build()
        XLog.init(config, AndroidPrinter())
    }
}

object AppContext : ContextWrapper(INSTANCE)

object CommonHelper {
    lateinit var context: Context
}