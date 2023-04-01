package com.seabreeze.robot.base.common

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import androidx.multidex.MultiDexApplication
import com.elvishew.xlog.LogConfiguration
import com.elvishew.xlog.XLog
import com.elvishew.xlog.printer.AndroidPrinter
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.seabreeze.robot.base.common.Settings.language_status
import com.seabreeze.robot.base.ext.initWebViewDataDirectory
import com.tencent.bugly.Bugly
import com.tencent.bugly.beta.Beta
import com.tencent.bugly.crashreport.CrashReport
import com.tencent.bugly.crashreport.CrashReport.UserStrategy
import com.tencent.mmkv.MMKV
import me.jessyan.autosize.AutoSize
import me.jessyan.autosize.AutoSizeConfig

/**
 * User: milan
 * Time: 2020/4/8 9:40
 * Des:
 */
lateinit var INSTANCE: Application

abstract class BaseApplication : MultiDexApplication() {

    override fun attachBaseContext(base: Context) {
        CommonHelper.context = base
        super.attachBaseContext(base)

        initWebViewDataDirectory()
    }

    override fun onCreate() {
        super.onCreate()

        INSTANCE = this

        //XLog
        val config = LogConfiguration.Builder()
            .tag("Base_XLog")
            .build()
        XLog.init(config, AndroidPrinter())

        val rootDir: String = MMKV.initialize(this)
        XLog.i(rootDir)

        AutoSize.initCompatMultiProcess(this)
        AutoSizeConfig.getInstance()

        //腾讯
//        CrashReport.initCrashReport(this, "a66e2b93f1", false)
//        CrashReport.setIsDevelopmentDevice(this, BuildConfig.DEBUG)
        val strategy = UserStrategy(this)
        strategy.setCrashHandleCallback(object : CrashReport.CrashHandleCallback() {
        })
        Bugly.init(this, buglyKey(), false, strategy)
        Beta.enableHotfix = false
        Beta.autoInit = true
        Beta.autoCheckUpgrade = true
        Beta.canShowUpgradeActs.add(canShowUpgradeAct())

        language_status.let {
            LanguageHelper.switchLanguage(this, it, isForce = true)
        }

        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, _ ->
            ClassicsHeader(context)
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, _ ->
            ClassicsFooter(context)
        }

    }

    abstract fun buglyKey(): String
    abstract fun canShowUpgradeAct(): Class<out Activity>

}

object AppContext : ContextWrapper(INSTANCE)

object CommonHelper {
    lateinit var context: Context
}