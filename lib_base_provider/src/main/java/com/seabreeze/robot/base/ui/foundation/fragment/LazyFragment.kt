package com.seabreeze.robot.base.ui.foundation.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.elvishew.xlog.XLog

abstract class LazyFragment : ImmersionFragment() {
    /**
     * Fragment生命周期 onAttach -> onCreate -> onCreatedView -> onActivityCreated
     * -> onStart -> onResume -> onPause -> onStop -> onDestroyView -> onDestroy
     * -> onDetach 对于 ViewPager + Fragment 的实现我们需要关注的几个生命周期有： onCreatedView +
     * onActivityCreated + onResume + onPause + onDestroyView
     */
    protected var rootView: View? = null

    /**
     * 布局是否创建完成
     */
    protected var isViewCreated = false

    /**
     * 当前可见状态
     */
    private var isSupportVisible = false
        set

    /**
     * 是否第一次可见
     */
    protected var mIsFirstVisible = true
    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    /**
     * 修改Fragment的可见性 setUserVisibleHint 被调用有两种情况：
     * 1）在切换tab的时候，会先于所有fragment的其他生命周期，先调用这个函数，可以看log 2)
     * 对于之前已经调用过setUserVisibleHint方法的fragment后，让fragment从可见到不可见之间状态的变化
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        XLog.d("setUserVisibleHint: $isVisibleToUser")
        // 对于情况1）不予处理，用 isViewCreated 进行判断，如果isViewCreated false，说明它没有被创建
        if (isViewCreated) {
            // 对于情况2,需要分情况考虑,如果是不可见 -> 可见 2.1
            // 如果是可见 -> 不可见 2.2
            // 对于2.1）我们需要如何判断呢？首先必须是可见的（isVisibleToUser
            // 为true）而且只有当可见状态进行改变的时候才需要切换，否则会出现反复调用的情况
            // 从而导致事件分发带来的多次更新
            if (isVisibleToUser && !isSupportVisible) {
                // 从不可见 -> 可见
                dispatchUserVisibleHint(true)
            } else if (!isVisibleToUser && isSupportVisible) {
                dispatchUserVisibleHint(false)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        if (null == rootView) {
            rootView = inflater.inflate(layoutRes, container, false)
        }
        initView(rootView)
        XLog.d("onCreateView: ")
        // 初始化的时候,判断当前Fragment可见状态
        // isHidden在使用FragmentTransaction的show/hidden时会调用,可见返回的是false
        if (!isHidden && userVisibleHint) {
            // 可见状态,进行事件分发
            dispatchUserVisibleHint(true)
        }
        return rootView
    }

    /**
     * 用FragmentTransaction来控制fragment的hide和show时，
     * 那么这个方法就会被调用。每当你对某个Fragment使用hide 或者是show的时候，那么这个Fragment就会自动调用这个方法。
     */
    override fun onHiddenChanged(hidden: Boolean) {
        XLog.d("onHiddenChanged: $hidden")
        super.onHiddenChanged(hidden)
        // 这里的可见返回为false
        if (hidden) {
            dispatchUserVisibleHint(false)
        } else {
            dispatchUserVisibleHint(true)
        }
    }

    /**
     * 统一处理用户可见事件分发
     */
    private fun dispatchUserVisibleHint(isVisible: Boolean) {
        XLog.d("dispatchUserVisibleHint: $isVisible")

        // 首先考虑一下fragment嵌套fragment的情况(只考虑2层嵌套)
        if (isVisible && isParentInvisible) {
            // 父Fragmnet此时不可见,直接return不做处理
            return
        }
        // 为了代码严谨,如果当前状态与需要设置的状态本来就一致了,就不处理了
        if (isSupportVisible == isVisible) {
            return
        }
        isSupportVisible = isVisible
        if (isVisible) {
            if (mIsFirstVisible) {
                mIsFirstVisible = false
                // 第一次可见,进行全局初始化
                onFragmentFirstVisible()
            }
            onFragmentResume()
            // 分发事件给内嵌的Fragment
            dispatchChildVisibleState(true)
        } else {
            onFragmentPause()
            dispatchChildVisibleState(false)
        }
    }

    /**
     * 在双重ViewPager嵌套的情况下，第一次滑到Frgment 嵌套ViewPager(fragment)的场景的时候
     * 此时只会加载外层Fragment的数据，而不会加载内嵌viewPager中的fragment的数据，因此，我们
     * 需要在此增加一个当外层Fragment可见的时候，分发可见事件给自己内嵌的所有Fragment显示
     */
    private fun dispatchChildVisibleState(visible: Boolean) {
        val fragmentManager = childFragmentManager
        val fragments = fragmentManager.fragments
        if (null != fragments) {
            for (fragment in fragments) {
                if (fragment is LazyFragment && !fragment.isHidden()
                    && fragment.getUserVisibleHint()
                ) {
                    fragment.dispatchUserVisibleHint(visible)
                }
            }
        }
    }

    /**
     * Fragment真正的Pause,暂停一切网络耗时操作
     */
    protected fun onFragmentPause() {
        XLog.d("onFragmentResume " + " 真正的resume,开始相关操作耗时")
    }

    /**
     * Fragment真正的Resume,开始处理网络加载等耗时操作
     */
    protected fun onFragmentResume() {
        XLog.d("onFragmentPause" + " 真正的Pause,结束相关操作耗时")
    }

    private val isParentInvisible: Boolean
        private get() {
            val parentFragment = parentFragment
            if (parentFragment is LazyFragment) {
                return !parentFragment.isSupportVisible
            }
            return false
        }

    /**
     * 在滑动或者跳转的过程中，第一次创建fragment的时候均会调用onResume方法
     */
    override fun onResume() {
        super.onResume()
        // 如果不是第一次可见
        if (!mIsFirstVisible) {
            // 如果此时进行Activity跳转,会将所有的缓存的fragment进行onResume生命周期的重复
            // 只需要对可见的fragment进行加载,
            if (!isHidden && !isSupportVisible && userVisibleHint) {
                dispatchUserVisibleHint(true)
            }
        }
    }

    /**
     * 只有当当前页面由可见状态转变到不可见状态时才需要调用 dispatchUserVisibleHint currentVisibleState &&
     * getUserVisibleHint() 能够限定是当前可见的 Fragment 当前 Fragment 包含子 Fragment 的时候
     * dispatchUserVisibleHint 内部本身就会通知子 Fragment 不可见 子 fragment 走到这里的时候自身又会调用一遍
     */
    override fun onPause() {
        super.onPause()
        if (isSupportVisible && userVisibleHint) {
            dispatchUserVisibleHint(false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        XLog.d("onDestroyView")
        isViewCreated = false
        mIsFirstVisible = false
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    /**
     * 第一次可见,根据业务进行初始化操作
     */
    protected abstract fun onFragmentFirstVisible()

    /**
     * 初始化页面,如果不需要进行懒加载则可直接在此处进行网络处理
     *
     * @param rootView
     */
    protected abstract fun initView(rootView: View?)

    /**
     * 获取布局资源
     */
    protected abstract val layoutRes: Int

    companion object {
        private const val TAG = "LazyFragment"
    }
}