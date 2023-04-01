package com.seabreeze.robot.data.db.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * User: milan
 * Time: 2020/4/9 9:17
 * Des:
 * 数据库示例
 */
@Entity(
    tableName = "work_mode_record"
)
data class WorkModeRecord(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,

    @ColumnInfo(name = "clean")
    val clean: Boolean,

    @ColumnInfo(name = "mop")
    val mop: Boolean
) 