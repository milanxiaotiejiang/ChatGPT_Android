package com.seabreeze.robot.base.ui.activity

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.framework.mvp.BasePresenter
import com.seabreeze.robot.base.framework.mvp.view.BaseView
import com.seabreeze.robot.base.ui.foundation.activity.RxAppCompatActivity
import java.lang.reflect.ParameterizedType

/**
 * User: milan
 * Time: 2020/3/24 16:35
 * Des: Mvp 推荐用 RxJava
 */
abstract class BaseMvpActivity<out Presenter : BasePresenter<BaseView<Presenter>>, VB : ViewBinding> :
    RxAppCompatActivity<VB>(), BaseView<Presenter> {

    final override val mPresenter: Presenter

    init {
        mPresenter = findPresenterClass().newInstance()
        mPresenter.mView = this
    }

    private fun findPresenterClass(): Class<Presenter> {
        var thisClass: Class<*> = this.javaClass
        while (true) {
            (thisClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments?.firstOrNull()
                ?.let {
                    return it as Class<Presenter>
                }
                ?: run {
                    thisClass = thisClass.superclass ?: throw IllegalArgumentException()
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPresenter.lifecycleProvider = this

        initData()
    }

    override fun onDestroy() {
        hideLoading()
        super.onDestroy()
    }

    protected abstract fun initData()

    override fun showToast(msg: String) {
        super.showToast(msg)
    }

    override fun showLoading(color: Int, tip: String, title: String) {
        showProgressDialog(title)
    }

    override fun hideLoading() {
        dismissProgressDialog()
    }

    override fun onError(throwable: BaseThrowable) {
        super.onError(throwable)
    }
}
