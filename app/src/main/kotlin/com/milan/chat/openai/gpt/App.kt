package com.milan.chat.openai.gpt

import android.content.Context
import com.elvishew.xlog.XLog
import com.milan.chat.openai.gpt.database.AppSettings.crashCount
import com.milan.chat.openai.gpt.ui.chat.ChatMessagesActivity
import com.seabreeze.robot.base.common.CommonHelper
import com.seabreeze.robot.base.common.defenselib.Crash
import com.seabreeze.robot.base.common.defenselib.DefenseCrash
import com.seabreeze.robot.base.ext.tool.isProdChannel
import com.seabreeze.robot.data.DataApplication
import io.reactivex.plugins.RxJavaPlugins

/**
 * User: milan
 * Time: 2023/3/30 20:02
 * Des:
 */
class App : DataApplication() {
    override fun attachBaseContext(base: Context) {
        CommonHelper.context = base
        super.attachBaseContext(base)
        if (isProdChannel()) {
            DefenseCrash.initialize(this)
            DefenseCrash.install { thread, throwable, isSafeMode, isCrashInChoreographer ->
                //isSafeMode: 如果应用程序崩溃过,但是被我们捕获,那么这个值将会是true来告知开发人员,
                //具体来讲就是当你的主线程(Main Looper)已经被错误破坏不能够正常loop的时候,我们将使用魔法保证他运行.这称之为安全模式
                //isCrashInChoreographer: 如果崩溃发生在 OnMeasure/OnLayout/OnDraw 方法中,这将会导致程序白屏或黑屏亦或是一些View显示不成功
                //当你收到这个值为True的时候,我们建议你关闭或者重启当前的Activity

                XLog.e(
                    """
                        DefenseCrash
                        ========================================================================================================
                        $throwable  
                        ${throwable.cause}${throwable.message}
                        ========================================================================================================
                    """
                )

                crashCount++
                if (isCrashInChoreographer || crashCount > 3) {
                    crashCount = 0
                    Crash.restartApp(this)
                }
            }
//        DefenseCrash.unInstall()
        }
    }

    override fun onCreate() {
        super.onCreate()

        if (isProdChannel()) {
            RxJavaPlugins.setErrorHandler { throwable: Throwable ->
                XLog.e(
                    """
                        RxJavaPlugins
                        ========================================================================================================
                        $throwable  
                        ${throwable.cause}${throwable.message}
                        ========================================================================================================
                    """
                )
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        XLog.e("onTerminate !!!")
    }

    override fun buglyKey() = "b2f65156eb"

    override fun canShowUpgradeAct() = ChatMessagesActivity::class.java
}