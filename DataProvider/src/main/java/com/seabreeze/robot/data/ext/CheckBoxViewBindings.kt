package com.seabreeze.robot.data.ext

import androidx.databinding.BindingAdapter
import com.seabreeze.robot.base.widget.SmoothCheckBox

/**
 * User: milan
 * Time: 2021/9/30 10:13
 * Des:
 */
@BindingAdapter("onCheckedChangeListener")
fun SmoothCheckBox.setOnCheckedChangeListener(listener: SmoothCheckBox.OnCheckedChangeListener) {
    setOnCheckedChangeListener(listener)
}

@BindingAdapter("setChecked")
fun SmoothCheckBox.setChecked(checked: Boolean) {
    isChecked = checked
}