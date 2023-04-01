package com.seabreeze.robot.base.ui.foundation.activity

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.TypedArray
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.elvishew.xlog.XLog
import com.gyf.immersionbar.ImmersionBar
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.common.AppManager
import com.seabreeze.robot.base.ext.find
import com.seabreeze.robot.base.ext.tool.getStatusBarHeight
import com.seabreeze.robot.base.ext.tool.setScreenLandscape
import com.seabreeze.robot.base.ext.tool.setScreenPortrait

/**
 * User: milan
 * Time: 2020/4/8 10:01
 * Des: 滑动返回、横竖屏、UI
 */
abstract class SwipeBackActivity : AppCompatActivity()  {

    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            val result: Boolean = fixOrientation()
            XLog.i("onCreate fixOrientation when Oreo, result = $result")
        }
        super.onCreate(savedInstanceState)
        //根类记录 Activity
        AppManager.addActivity(this)
        //横竖屏
        if (defaultScreen()) {
            setScreenPortrait()
        } else {
            setScreenLandscape()
        }
    }

    open fun defaultScreen() = true

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            XLog.i("avoid calling setRequestedOrientation when Oreo.")
            return
        }
        super.setRequestedOrientation(requestedOrientation)
    }

    private fun isTranslucentOrFloating(): Boolean {
        var isTranslucentOrFloating = false
        try {
            val styleableRes = Class.forName("com.android.internal.R\$styleable")
                .getField("Window")[null] as IntArray
            val ta = obtainStyledAttributes(styleableRes)
            val m =
                ActivityInfo::class.java.getMethod(
                    "isTranslucentOrFloating",
                    TypedArray::class.java
                )
            m.isAccessible = true
            isTranslucentOrFloating = m.invoke(null, ta) as Boolean
            m.isAccessible = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isTranslucentOrFloating
    }

    private fun fixOrientation(): Boolean {
        try {
            val field = Activity::class.java.getDeclaredField("mActivityInfo")
            field.isAccessible = true
            val o = field[this] as ActivityInfo
            o.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
            field.isAccessible = false
            return true
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        //屏幕设置
        initImmersionUi()
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        //屏幕设置
        initImmersionUi()
    }

    override fun onDestroy() {
        AppManager.finishActivity(this)
        super.onDestroy()
    }

    //获取Window中视图content
    val contentView: View
        get() {
            val content = find<FrameLayout>(android.R.id.content)
            return content.getChildAt(0)
        }

    protected open fun initImmersionUi() {
        if (booHideBottom()) {
            //隐藏虚拟按键，并且全屏
            val window = window
            val params = window.attributes
            params.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
            window.attributes = params
        }

        //status Bar 沉浸式状态栏相关设置
        if (isImmersionBar()) {
            setImmersionBar()
        }
    }

    protected open fun booHideBottom() = false

    protected open fun isImmersionBar() = true

    protected open fun setImmersionBar() {
        ImmersionBar.with(this)
            .keyboardEnable(true)
            .titleBarMarginTop(R.id.toolbar)
            .statusBarDarkFont(true)
            .navigationBarColor(android.R.color.white) //导航栏颜色，不写默认黑色
            .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
            .init()
    }

    protected open fun immersionNavigationBar() {
        ImmersionBar.with(this)
            .navigationBarColor(android.R.color.white) //导航栏颜色，不写默认黑色
            .navigationBarDarkIcon(true) //导航栏图标是深色，不写默认为亮色
            .init()
    }

    protected open fun setStatusBar(view: View) {
        val statusHeight = getStatusBarHeight()
        view.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            statusHeight
        )
    }

}
