package com.milan.chat.openai.gpt.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.TextView
import androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable
import androidx.fragment.app.DialogFragment
import com.milan.chat.openai.gpt.R
import com.milan.chat.openai.gpt.ext.fullScreen
import com.scwang.smart.refresh.header.material.CircleImageView
import com.scwang.smart.refresh.header.material.MaterialProgressDrawable

/**
 * <pre>
 * @user : milanxiaotiejiang
 * @email : 765151629@qq.com
 * @version : 1.0
 * @date : 2020/9/11
 * @description : 等待框 loading ...
 * </pre>
 */
class ProgressDialogFragment : DialogFragment() {

    companion object {

        const val CIRCLE_BG_LIGHT = -0x50506
        const val MAX_PROGRESS_ANGLE = .8f

        fun newInstance() = ProgressDialogFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_FRAME, R.style.Dialog_FullScreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_progress_dialog, container, false)
    }

    private var mMessage: String? = null
    private var mProgress: MaterialProgressDrawable? = null
    private var mCircleView: CircleImageView? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvMessage = view.findViewById<TextView>(R.id.tvMessage)
        tvMessage.text = mMessage ?: getString(R.string.loading)

        val frameLayout = view.findViewById<FrameLayout>(R.id.frameLayout)

        mProgress = MaterialProgressDrawable(frameLayout)
            .apply {
                setColorSchemeColors(-0xff6634, -0xbbbc, -0x996700, -0x559934, -0x7800)
                showArrow(true)
                setStartEndTrim(0f, MAX_PROGRESS_ANGLE)
                setArrowScale(1f)
            }

        mCircleView = CircleImageView(requireContext(), CIRCLE_BG_LIGHT)
            .apply {
                setImageDrawable(mProgress)
                alpha = 1f
                visibility = View.VISIBLE
            }


        frameLayout.addView(mCircleView)

        mProgress?.start()
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            activity?.apply {
                window?.apply {
                    val windowParams = attributes
                    windowParams.dimAmount = 0.7f //将Window周边设置透明为0.7
//                    setCanceledOnTouchOutside(false) //点击周边不隐藏对话框
                    attributes = windowParams
                    setGravity(Gravity.CENTER)
                }
            }
        }
    }

    override fun onDestroyView() {
        mProgress?.stop()
        mProgress = null
        mCircleView?.setImageDrawable(null)
        view?.apply {
            if (this is ViewGroup) {
                removeAllViews()
            }
        }
        super.onDestroyView()
    }

    fun showMessage(
        message: String?,
        isCancelable: Boolean = false
    ) {
        this.mMessage = message
        this.isCancelable = isCancelable
    }

}