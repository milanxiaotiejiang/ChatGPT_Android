package com.seabreeze.robot.data.db.bean

import androidx.room.*

/**
 * User: milan
 * Time: 2020/4/9 9:17
 * Des:
 * 数据库示例
 */
@Entity(
    tableName = "system_count",
    indices = [Index(value = ["count"])]
)
data class SystemCount @Ignore constructor(
    //名称
    @PrimaryKey
    @ColumnInfo(name = "title")
    var title: String,

    //个数
    @ColumnInfo(name = "count")
    var count: Long = 0,

    //年月日
    @ColumnInfo(name = "year")
    var year: Int = 0,
    @ColumnInfo(name = "month")
    var month: Int = 0,
    @ColumnInfo(name = "day")
    var day: Int = 0
) {
    constructor() : this(title = "")

    override fun toString(): String {
        return "VoiceCount(title='$title', count=$count, year=$year, month=$month, day=$day)"
    }
}