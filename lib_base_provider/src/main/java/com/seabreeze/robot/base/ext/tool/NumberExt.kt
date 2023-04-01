package com.seabreeze.robot.base.ext.tool

import java.text.NumberFormat

/**
 * <pre>
 * author : 76515
 * time   : 2020/7/4
 * desc   :
 * </pre>
 */
/**
 * 去除小数点后面的0
 * @see removeLastZero
 */
fun String?.removeLastZero(isGroupingUsed: Boolean = false) = when (isNullOrEmpty()) {
    true -> ""
    false -> this.toDouble().removeLastZero(isGroupingUsed)
}

/**
 * 去除小数点后面的0
 * @param isGroupingUsed:是否使用千分分隔符，默认为false不使用
 */
fun Double?.removeLastZero(isGroupingUsed: Boolean = false) = when (this == null) {
    true -> ""
    false -> {
        NumberFormat.getInstance().let {
            it.isGroupingUsed = isGroupingUsed
            it.format(this) ?: ""
        }
    }
}

/**
 * 计算百分比 保留n位小数
 */
fun proportionDouble(divisor: Int, dividend: Int, bit: Int = 0): String {
    if (dividend == 0) {
        return "0"
    }
    val numberFormat: NumberFormat = NumberFormat.getInstance()
    numberFormat.maximumFractionDigits = bit // 设置精确到小数点后bit位
    val result: String = numberFormat.format(divisor!!.toFloat() / dividend!!.toFloat() * 100)
    return "$result"
}