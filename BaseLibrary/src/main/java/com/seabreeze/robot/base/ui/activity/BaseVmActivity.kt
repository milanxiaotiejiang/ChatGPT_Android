package com.seabreeze.robot.base.ui.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.seabreeze.robot.base.ext.coroutine.observe
import com.seabreeze.robot.base.ext.foundation.onError
import com.seabreeze.robot.base.framework.mvvm.BaseViewModel
import com.seabreeze.robot.base.framework.mvvm.IViewModel
import com.seabreeze.robot.base.ui.foundation.activity.BaseActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.lang.reflect.ParameterizedType


/**
 * User: milan
 * Time: 2020/3/24 16:35
 * Des: Mvvm 必须使用协程
 */
abstract class BaseVmActivity<out ViewModel : BaseViewModel, DataBinding : ViewDataBinding>(
    @LayoutRes
    private val layoutId: Int
) : BaseActivity(), IViewModel<ViewModel> {

    lateinit var mDataBinding: DataBinding

    final override val mViewModel: ViewModel

    //rxJava 自行实现
    private lateinit var mCompositeDisposable: CompositeDisposable

    init {
        mViewModel = findViewModelClass().newInstance()
    }

    private fun findViewModelClass(): Class<ViewModel> {
        var thisClass: Class<*> = this.javaClass
        while (true) {
            (thisClass.genericSuperclass as? ParameterizedType)?.actualTypeArguments?.firstOrNull()
                ?.let {
                    return it as Class<ViewModel>
                }
                ?: run {
                    thisClass = thisClass.superclass ?: throw IllegalArgumentException()
                }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mDataBinding = DataBindingUtil.setContentView(this, layoutId)
        mDataBinding.lifecycleOwner = this

        mCompositeDisposable = CompositeDisposable()

        onInitDataBinding()

        initViewModelActions()
        initData()
    }

    abstract fun onInitDataBinding()

    open fun initViewModelActions() {

    }

    protected abstract fun initData()

    protected fun registerErrorEvent() = observe(mViewModel.uiLiveEvent.errorEvent) { it.onError() }

    protected fun registerLoadingProgressBarEvent() =
        with(mViewModel.uiLiveEvent) {
            observe(showLoadingProgressBarEvent) { showProgressDialog() }
            observe(dismissLoadingProgressBarEvent) { dismissProgressDialog() }
        }

    /**
     * 添加订阅
     */
    open fun addDisposable(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    /**
     * 取消所有订阅
     */
    open fun clearDisposable() {
        mCompositeDisposable.clear()
    }
}