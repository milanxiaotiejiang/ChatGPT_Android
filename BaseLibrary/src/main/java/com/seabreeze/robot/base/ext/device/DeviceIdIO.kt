package com.seabreeze.robot.base.ext.device

import android.content.Context
import com.orhanobut.logger.Logger
import com.seabreeze.robot.base.common.CommonHelper.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.*

//保存文件的路径
private const val CACHE_IMEI_DIR = "devices/id/file"

//保存的文件 采用隐藏文件的形式进行保存
private const val DEVICES_FILE_NAME = ".DEVICES"


/**
 * 读取固定的文件中的内容,这里就是读取sd卡中保存的设备唯一标识符
 */
fun Context.readDeviceID(): String? {
    try {
        val file: File = getDevicesDir()
        val buffer = StringBuffer()
        val fis = FileInputStream(file)
        val inputStreamReader = InputStreamReader(fis, "UTF-8")
        val bufferedReader = BufferedReader(inputStreamReader)
        var i: Int
        while (bufferedReader.read().also { i = it } > -1) {
            buffer.append(i.toChar())
        }
        bufferedReader.close()
        return buffer.toString()

    } catch (e: Exception) {
        return null
    }
}


/**
 * 保存 内容到 SD卡中,  这里保存的就是 设备唯一标识符
 * @param str
 * @param context
 */
fun Context.saveDeviceID(str: String?) {
    GlobalScope.launch(Dispatchers.IO) {
        val file: File = getDevicesDir()
        try {
            val fos = FileOutputStream(file)
            val out: Writer = OutputStreamWriter(fos, "UTF-8")
            out.write(str)
            out.close()
        } catch (e: Exception) {
            Logger.e("存储唯一识别码失败")
        }
    }
}

/**
 * 统一处理设备唯一标识 保存的文件的地址
 */
private fun Context.getDevicesDir(): File {
    val cropFile: File
//        /data/data/com.my.app/files
    val cropDir = File(filesDir, CACHE_IMEI_DIR)
    //如果文件不存在，创建一个
    if (!cropDir.exists()) cropDir.mkdirs()
    cropFile = File(cropDir, DEVICES_FILE_NAME)
    return cropFile
}
