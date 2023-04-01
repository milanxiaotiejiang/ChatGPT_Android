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
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper
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
abstract class SwipeBackActivity : AppCompatActivity(), BGASwipeBackHelper.Delegate {

    private lateinit var mSwipeBackHelper: BGASwipeBackHelper

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
        //滑动返回
        initSwipeBackFinish()
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

    override fun onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding) {
            return
        }
        mSwipeBackHelper.backward()
    }

    private fun initSwipeBackFinish() {
        mSwipeBackHelper = BGASwipeBackHelper(this, this)

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。
        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true)
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true)
        // 设置是否是微信滑动返回样式。默认值为 true
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true)
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow)
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true)
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true)
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f)
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false)
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

    override fun onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward()
    }

    override fun onSwipeBackLayoutSlide(slideOffset: Float) {
        XLog.i("onSwipeBackLayoutSlide $slideOffset")
    }

    override fun onSwipeBackLayoutCancel() {
        XLog.i("onSwipeBackLayoutCancel")
    }

    override fun isSupportSwipeBack() = true

}
