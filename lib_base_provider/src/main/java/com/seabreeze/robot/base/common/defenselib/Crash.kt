package com.seabreeze.robot.base.common.defenselib

import android.content.Context
import android.content.Intent
import android.os.Process
import com.seabreeze.robot.base.common.AppManager
import kotlin.system.exitProcess

/**
 * User: milan
 * Time: 2022/5/26 13:20
 * Des:
 */
object Crash {
    fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        AppManager.exitApp(context)
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }
}