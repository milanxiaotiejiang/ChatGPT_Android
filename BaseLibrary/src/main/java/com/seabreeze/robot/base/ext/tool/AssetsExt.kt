package com.seabreeze.robot.base.ext.tool

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader

/**
 * User: milan
 * Time: 2021/9/6 13:10
 * Des:
 */

fun Context.loadFromAssets(fileName: String): String {
    val inputStreamReader = InputStreamReader(assets.open(fileName))
    val mapReader = BufferedReader(inputStreamReader)
    val bufReader = BufferedReader(mapReader)
    var line: String?
    val stringBuilder = StringBuilder()
    while (bufReader.readLine().also { line = it } != null) stringBuilder.append(line)
    return stringBuilder.toString()
}