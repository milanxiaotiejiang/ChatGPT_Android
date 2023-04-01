package com.seabreeze.robot.base.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.viewbinding.ViewBinding
import com.seabreeze.robot.base.ext.foundation.BaseThrowable
import com.seabreeze.robot.base.framework.mvp.BasePresenter
import com.seabreeze.robot.base.framework.mvp.view.BaseView
import com.seabreeze.robot.base.ui.foundation.fragment.RxFragment
import com.seabreeze.robot.base.widget.CircularView
import java.lang.reflect.ParameterizedType

/**
 * User: milan
 * Time: 2020/4/8 10:01
 * Des:
 */
abstract class BaseMvpFragment<out Presenter : BasePresenter<BaseView<Presenter>>, VB : ViewBinding> :
    RxFragment<VB>(), BaseView<Presenter> {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.lifecycleProvider = this
    }

    private lateinit var loadView: View
    private lateinit var circularView: CircularView
    private lateinit var loadTip: TextView

    override fun onDestroyView() {
        hideLoading()
        super.onDestroyView()
    }

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
