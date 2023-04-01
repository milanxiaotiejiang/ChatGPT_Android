package com.seabreeze.robot.base.ui.foundation.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toolbar
import com.alibaba.android.arouter.launcher.ARouter
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import com.permissionx.guolindev.PermissionMediator
import com.permissionx.guolindev.PermissionX
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.common.AppManager
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.ext.foundation.onError
import com.seabreeze.robot.base.ext.tool.toast
import com.seabreeze.robot.base.ui.fragment.ProgressDialogFragment
import com.seabreeze.robot.base.widget.bar.OnTitleBarListener
import com.seabreeze.robot.base.widget.bar.TitleBar
import com.seabreeze.robot.base.widget.dialog.ForceUploadDialog

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2021/7/30
 * @description : toast、dialog、error
 * </pre>
 */
abstract class BaseActivity : InternationalizationActivity() {

    protected open val mMainHandler = Handler(Looper.getMainLooper())

    protected open lateinit var mPermission: PermissionMediator

    private lateinit var progressDialogFragment: ProgressDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //ARouter注册
        ARouter.getInstance().inject(this)

        mPermission = PermissionX.init(this)

        progressDialogFragment = ProgressDialogFragment.newInstance()
    }

    override fun initImmersionUi() {
        super.initImmersionUi()
        findViewById<View>(R.id.toolbar)?.apply {
            when (this) {
                is Toolbar -> {
                    setNavigationOnClickListener {
                        finish()
                    }
                }
                is TitleBar -> {
                    setOnTitleBarListener(object : OnTitleBarListener {
                        override fun onLeftClick(view: View) {
                            finish()
                        }
                    })
                }
            }
        }
    }

    override fun onDestroy() {
        dismissProgressDialog()
        super.onDestroy()
    }

    /**
     * 显示加载(转圈)对话框
     */
    open fun showProgressDialog(message: String? = null) {
        if (!this::progressDialogFragment.isInitialized) {
            progressDialogFragment = ProgressDialogFragment.newInstance()
        }
        if (!progressDialogFragment.isAdded) {
            val beginTransaction = supportFragmentManager.beginTransaction()
            val dialogFragment = supportFragmentManager.findFragmentByTag("progressDialogFragment")
            if (dialogFragment != null) {
                beginTransaction.remove(dialogFragment)
            }
            progressDialogFragment.showMessage(message, true)
            beginTransaction.add(progressDialogFragment, "progressDialogFragment")
            beginTransaction.addToBackStack(null)
            beginTransaction.commitAllowingStateLoss()
//            progressDialogFragment.show(supportFragmentManager, "progressDialogFragment")
        }
    }

    /**
     * 隐藏加载(转圈)对话框
     */
    open fun dismissProgressDialog() {
        if (this::progressDialogFragment.isInitialized && progressDialogFragment.isVisible) {

            val beginTransaction = supportFragmentManager.beginTransaction()
            val dialogFragment = supportFragmentManager.findFragmentByTag("progressDialogFragment")
            if (dialogFragment != null) {
                beginTransaction.remove(dialogFragment)
            }
            beginTransaction.commitAllowingStateLoss()
//        progressDialogFragment.dismissAllowingStateLoss()
        }
    }

    open fun onError(throwable: BaseThrowable) {
        dismissProgressDialog()
        throwable.onError()
    }

    open fun showToast(msg: String) {
//        Alerter.create(this@BaseMvpActivity)
//            .setText(msg)
//            .show()
        runOnUiThread { toast { msg } }
    }

    fun showKickDialog() {
        val activity = AppManager.currentActivity()
        if (activity === this) {
            val kickEachDialog = ForceUploadDialog(this)
            XPopup.Builder(this)
                .hasShadowBg(true)
                .hasBlurBg(true)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .asCustom(kickEachDialog)
                .show()
        }
    }

    fun showBasePopupView(basePopupView: BasePopupView, offsetX: Int = 0, offsetY: Int = 0) {
        val activity = AppManager.currentActivity()
        if (activity === this) {
            XPopup.Builder(this)
                .hasShadowBg(true)
                .hasBlurBg(true)
                .dismissOnBackPressed(false)
                .dismissOnTouchOutside(false)
                .offsetX(offsetX)
                .offsetY(offsetY)
                .asCustom(basePopupView)
                .show()
        }
    }

}