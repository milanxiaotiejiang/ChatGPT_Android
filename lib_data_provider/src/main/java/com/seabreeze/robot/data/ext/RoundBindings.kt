package com.seabreeze.robot.data.ext

import androidx.databinding.BindingAdapter
import com.seabreeze.robot.base.ext.tool.getResColor
import com.seabreeze.robot.base.widget.round.RoundLinearLayout
import com.seabreeze.robot.data.R

/**
 * User: milan
 * Time: 2022/5/13 14:57
 * Des:
 */
@BindingAdapter("selectBackgroundColor")
fun RoundLinearLayout.setSelectBackgroundColor(select: Boolean) {
    delegate.backgroundColor = if (select)
        getResColor(R.color.color17D6D6)
    else
        getResColor(android.R.color.white)
}