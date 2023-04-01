package com.seabreeze.robot.base.ui.foundation.activity

import android.content.Context
import android.os.Bundle
import android.view.View
import com.seabreeze.robot.base.common.LanguageHelper
import com.seabreeze.robot.base.common.Settings
import com.seabreeze.robot.base.ext.tool.setFullScreen

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/1
 * desc   : 主题、国际化
 * </pre>
 */
abstract class InternationalizationActivity : SwipeBackActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        //主题适配
        setTheme(initTheme())
        super.onCreate(savedInstanceState)
        if (!defaultScreen()) {
            //隐藏虚拟按键，并且全屏
            val window = window
            val params = window.attributes
            params.systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE
            window.attributes = params
            setFullScreen()
        }
    }

    override fun attachBaseContext(newBase: Context) {
        //语言国际化
        val context = Settings.language_status.let {
            LanguageHelper.switchLanguage(newBase, it, isForce = true)
        }
        super.attachBaseContext(context)
    }

    open fun initTheme(): Int {
        return Settings.project_theme
    }
}