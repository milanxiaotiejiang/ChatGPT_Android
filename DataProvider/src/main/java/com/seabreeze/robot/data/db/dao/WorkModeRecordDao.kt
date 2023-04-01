package com.seabreeze.robot.data.db.dao

import androidx.room.*
import com.seabreeze.robot.data.db.bean.WorkModeRecord

/**
 * User: milan
 * Time: 2020/4/9 10:51
 * Des:
 */
@Dao
interface WorkModeRecordDao {
    //增
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: WorkModeRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(objs: List<WorkModeRecord>)

    //删
    @Delete
    fun delete(obj: WorkModeRecord): Int

    @Query("DELETE FROM 'work_mode_record'")
    fun deleteAll()

    //改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(obj: WorkModeRecord): Int

    //查
    @Query("SELECT * FROM 'work_mode_record'")
    fun loadAll(): List<WorkModeRecord>

    @Query("SELECT * FROM 'work_mode_record' WHERE id = :id")
    fun queryByKey(id: Long): WorkModeRecord?

}