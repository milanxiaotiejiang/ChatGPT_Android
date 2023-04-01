package com.seabreeze.robot.base.common

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import com.tencent.bugly.proguard.K
import kotlin.system.exitProcess

/**
 * User: milan
 * Time: 2020/4/8 9:39
 * Des:
 */
object AppManager {

    private val activitySet: java.util.LinkedHashMap<String, Activity> = java.util.LinkedHashMap()

    /**
     * 当前Activity标记
     */
    private var curTag = ""

    /**
     * Activity入栈
     */
    @JvmStatic
    fun addActivity(activity: Activity) {
        curTag = getObjectTag(activity)
        activitySet[curTag] = activity
    }

    /**
     * Activity出栈
     */
    @JvmStatic
    fun removeActivity(activity: Activity): Activity? {
        val remove = activitySet.remove(getObjectTag(activity))
        if (activitySet.isNotEmpty()) {
            val tailByReflection = activitySet.getTail()
            curTag = tailByReflection?.key ?: ""
        }
        return remove
    }

    private fun <K, V> LinkedHashMap<K, V>.getTail(): Map.Entry<K, V>? {
        val iterator = entries.iterator()
        var tail: Map.Entry<K, V>? = null
        while (iterator.hasNext()) {
            tail = iterator.next()
        }
        return tail
    }

    /**
     * Activity出栈
     */
    fun removeActivity(vararg clazz: Class<*>) {
        clazz.forEach { clz ->
            activitySet.forEach {
                if (it.value.javaClass == clz) {
                    removeActivity(it.value)
                }
            }
        }
    }

    /**
     * 销毁一个Activity并出栈
     */

    @JvmStatic
    @Synchronized
    fun finishActivity(activity: Activity): Activity? {
        if (!activity.isDestroyed) {
            activity.finish()
        }
        return removeActivity(activity)
    }

    /**
     * 销毁Activity并出栈
     */
    @JvmStatic
    fun finishActivity(vararg clazz: Class<*>) {
        val keys = activitySet.keys
        clazz.forEach { clz ->
            keys.forEach {
                val activity = activitySet[it]
                if (activity != null && activity.javaClass == clz) {
                    finishActivity(activity)
                }
            }
        }

    }

    /**
     * 销毁除了给定的Activity外的所有Activity
     * @param clazz:Activity白名单
     */
    @JvmStatic
    fun finishExceptActivity(vararg clazz: Class<*>) {
        val keys = activitySet.keys
        clazz.forEach { clz ->
            keys.forEach {
                val activity = activitySet[it]
                if (activity != null && activity.javaClass != clz) {
                    finishActivity(activity)
                }
            }
        }
    }

    /**
     * 获取当前栈顶Activity
     * @return 如果栈内元素为空，则返回null
     */
    @JvmStatic
    fun currentActivity(): Activity? {
        return if (activitySet.isNotEmpty())
            activitySet[curTag]
        else
            null
    }


    /**
     * 清理栈中所有的Activity
     */
    @JvmStatic
    fun finishAllActivity() {
        activitySet.forEach {
            if (!it.value.isDestroyed) {
                it.value.finish()
            }
        }
        activitySet.clear()
    }

    /**
     * 退出应用程序
     */
    @JvmStatic
    fun exitApp(context: Context) {
        finishAllActivity()
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        activityManager.killBackgroundProcesses(context.packageName)
        exitProcess(0)
    }

    /**
     * 设置栈中Activity的key
     */
    private fun getObjectTag(activity: Activity) =
        activity.javaClass.name + Integer.toHexString(activity.hashCode())
}