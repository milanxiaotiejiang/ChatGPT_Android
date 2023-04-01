package com.seabreeze.robot.base.widget.dialog

import android.content.Context
import android.widget.ImageView
import android.widget.TextView
import com.lxj.xpopup.core.CenterPopupView
import com.seabreeze.robot.base.R
import com.seabreeze.robot.base.ext.find

/**
 * User: milan
 * Time: 2022/4/1 18:28
 * Des:
 */
class ForceUploadDialog(context: Context) : CenterPopupView(context) {
    override fun getImplLayoutId() = R.layout.dialog_force_upload

    override fun onCreate() {
        super.onCreate()

        val tvConfirm = findViewById<TextView>(R.id.tvConfirm)
        tvConfirm.setOnClickListener {
        }
    }
}

class LowBatteryDialog(context: Context) : CenterPopupView(context) {

    lateinit var mPressListener: OnClickListener

    override fun getImplLayoutId() = R.layout.dialog_low_battery

    override fun onCreate() {
        super.onCreate()

        val tvConfirm = findViewById<TextView>(R.id.tvConfirm)
        tvConfirm.setOnClickListener {
            if (this::mPressListener.isInitialized)
                mPressListener.onClick(this)
            dismiss()
        }
    }

}

class ConsumableTipDialog(context: Context) : CenterPopupView(context) {

    private lateinit var tvConfirm: TextView
    private lateinit var ivIcon: ImageView
    private lateinit var tvValue: TextView
    private lateinit var tvUnit: TextView
    private lateinit var tvTipContent: TextView

    private var mIconResource: Int? = null
    private var mValue: Int? = null
    private var mUnit: CharSequence? = null
    private var mTipText: CharSequence? = null

    lateinit var mPressListener: OnClickListener

    override fun getImplLayoutId() = R.layout.dialog_consumable_tip

    override fun onCreate() {
        super.onCreate()

        tvConfirm = find(R.id.tv_confirm)
        ivIcon = find(R.id.iv_icon)
        tvValue = find(R.id.tv_value)
        tvUnit = find(R.id.tv_unit)
        tvTipContent = find(R.id.tv_tip_content)

        mIconResource?.apply { ivIcon.setImageResource(this) }
        mValue?.apply { tvValue.text = "$this" }
        mUnit?.apply { tvUnit.text = this }
        mTipText?.apply { tvTipContent.text = this }

        tvConfirm.setOnClickListener {
            if (this::mPressListener.isInitialized)
                mPressListener.onClick(this)
            dismiss()
        }
    }

    fun setTipContent(
        icon: Int? = null,
        value: Int? = null,
        unit: CharSequence? = null,
        tipText: CharSequence? = null
    ): ConsumableTipDialog {
        this.mIconResource = icon
        this.mValue = value
        this.mUnit = unit
        this.mTipText = tipText
        return this
    }
}