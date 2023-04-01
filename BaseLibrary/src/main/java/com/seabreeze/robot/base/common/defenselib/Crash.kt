package com.seabreeze.robot.base.common.defenselib

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Process
import com.elvishew.xlog.XLog
import com.seabreeze.robot.base.common.AppManager
import com.seabreeze.robot.base.ext.tool.diskCachePath
import java.io.File
import java.io.FileOutputStream
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.system.exitProcess

/**
 * User: milan
 * Time: 2022/5/26 13:20
 * Des:
 */
object Crash {
    private var crashInfoMap: MutableMap<String, String> = HashMap()
    private var formatter = SimpleDateFormat("yyyy-MM-dd HH-mm-ss")

    fun putCrashInfo(key: String, value: String) {
        crashInfoMap[key] = value
    }

    fun saveCrashInfo2File(app: Application, ex: Throwable) {
        val sb = StringBuffer()
        crashInfoMap.forEach { (t, u) ->
            sb.append("$t=$u\n")
        }

        val writer = StringWriter()
        val printWriter = PrintWriter(writer)
        ex.printStackTrace(printWriter)
        var cause = ex.cause
        while (cause != null) {
            cause.printStackTrace(printWriter)
            cause = cause.cause
        }
        printWriter.close()
        val result = writer.toString()
        sb.append(result)
        try {
            val diskCachePath = app.diskCachePath()
            val path =
                "${diskCachePath}/crash/${formatter.format(Date())}-${System.currentTimeMillis()}.log}"
            val dir = File(path)
            if (!dir.exists()) {
                dir.createNewFile()
            }
            val fos = FileOutputStream(path)
            fos.write(sb.toString().toByteArray())
            fos.close()
        } catch (e: Exception) {
            XLog.e("an error occured while writing file...", e)
        }
    }

    fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
            ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
        AppManager.exitApp(context)
        Process.killProcess(Process.myPid())
        exitProcess(0)
    }
}