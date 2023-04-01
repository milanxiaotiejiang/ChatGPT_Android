package com.seabreeze.robot.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.seabreeze.robot.data.db.bean.EditRecord
import com.seabreeze.robot.data.db.bean.SystemCount
import com.seabreeze.robot.data.db.bean.WorkModeRecord
import com.seabreeze.robot.data.db.dao.EditRecordDao
import com.seabreeze.robot.data.db.dao.SystemCountDao
import com.seabreeze.robot.data.db.dao.WorkModeRecordDao

/**
 * User: milan
 * Time: 2020/4/9 9:01
 * Des:
 */
@Database(
    entities = [
        SystemCount::class,
        WorkModeRecord::class,
        EditRecord::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun systemCount(): SystemCountDao
    abstract fun workModeRecord(): WorkModeRecordDao
    abstract fun editRecord(): EditRecordDao
}