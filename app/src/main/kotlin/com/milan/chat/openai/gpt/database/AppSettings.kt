package com.milan.chat.openai.gpt.database

import com.seabreeze.robot.base.ext.foundation.Mmkv

/**
 * User: milan
 * Time: 2023/4/1 13:29
 * Des:
 */
object AppSettings {
    var crashCount: Int by Mmkv("CRASH_COUNT", 0)
}