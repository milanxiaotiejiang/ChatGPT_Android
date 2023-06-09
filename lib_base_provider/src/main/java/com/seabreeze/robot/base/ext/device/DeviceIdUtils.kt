package com.seabreeze.robot.base.ext.device

import android.content.Context
import android.text.TextUtils
import com.elvishew.xlog.XLog
import java.util.*


fun Context.getDeviceId(
    upperCase: Boolean = false,
    useSSAID: Boolean = true,
    useMd5: Boolean = true
): String {
    // 1.读取保存的在sd卡中的唯一标识符
    var deviceId = readDeviceID()

    //判断是否已经生成过,生成过，就直接返回
//    if (!TextUtils.isEmpty(deviceId)) {
//        return deviceId!!
//    }

    // 2.1 开始获取设备唯一识别码

    //获取IMEI(也就是常说的DeviceId)
    try {
        deviceId = getIMEI()
    } catch (e: Exception) {
        XLog.i("获取设备唯一识别码失败,即将开始获取设备MAC地址")
    }

    // 2.2 开始获取设备MAC地址
    if (TextUtils.isEmpty(deviceId)) {
        XLog.i("获取设备唯一识别码失败,即将开始获取设备MAC地址")
        try {
            deviceId = getMAC().replace(":", "")
        } catch (e: Exception) {
            XLog.i("获取设备MAC地址失败,即将开始获取SSAID")
        }
    } else
        XLog.d("获取设备IMEI成功")

    // 2.3 获取SSAID，如果使用者不将获取SSAID纳入考虑范围，则跳过此步骤
    if (TextUtils.isEmpty(deviceId) && useSSAID) {
        try {
            deviceId = getSSAID()
        } catch (e: Exception) {
            XLog.i("获取SSAID失败,即将开始获取UUID")
        }
    } else
        XLog.d("获取设备MAC成功")

    // 2.4 如果以上都没有获取相应的唯一识别码,则自己生成相应的UUID作为相应设备唯一标识符

    if (TextUtils.isEmpty(deviceId)) {
        XLog.i("获取设备MAC地址失败,即将开始获取UUID")
        val uuid = UUID.randomUUID()
        deviceId = uuid.toString().replace("-", "")
    } else
        XLog.d("获取设备SSAID成功")

    // 3.为了统一格式对设备的唯一标识进行md5加密 最终生成32位字符串,如果不使用，则跳过,直接使用获取到的设备MAC进行存储
    var strMD5 = ""
    if (useMd5) {
        strMD5 = DeviceIdEncode.encodeMD5(deviceId!!, upperCase)
        if (strMD5.isNotEmpty()) {
            //持久化操作, 进行保存到内部存储中
            saveDeviceID(strMD5)
        }
    } else {
        saveDeviceID(deviceId)
    }

    // 4.返回MD5加密后的字符串
    return if (useMd5) strMD5 else deviceId!!
}
