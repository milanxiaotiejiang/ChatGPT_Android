package com.seabreeze.robot.data.db.bean

import androidx.room.*
import androidx.room.ForeignKey.Companion.CASCADE

/**
 * User: milan
 * Time: 2020/4/9 9:17
 * Des:
 * 数据库示例
 */
@Entity(
    tableName = "edit_record",
    indices = [Index(value = ["mId"])],
    foreignKeys = [
        ForeignKey(
            entity = WorkModeRecord::class,
            parentColumns = ["id"],
            childColumns = ["mId"],
            onDelete = CASCADE
        )]
)
data class EditRecord constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "eId")
    val eId: Long,

    @ColumnInfo(name = "mId")
    val mId: Long,

    @ColumnInfo(name = "type")
    val type: Int,

    @ColumnInfo(name = "json")
    val json: String
)