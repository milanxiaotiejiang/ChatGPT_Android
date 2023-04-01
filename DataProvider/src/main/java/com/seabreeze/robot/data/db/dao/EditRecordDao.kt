package com.seabreeze.robot.data.db.dao

import androidx.room.*
import com.seabreeze.robot.data.db.bean.EditRecord

/**
 * User: milan
 * Time: 2020/4/9 10:51
 * Des:
 */
@Dao
interface EditRecordDao {
    //增
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(obj: EditRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(objs: List<EditRecord>)

    //删
    @Delete
    fun delete(obj: EditRecord): Int

    @Query("DELETE FROM 'edit_record'")
    fun deleteAll()

    //改
    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(obj: EditRecord): Int

    //查
    @Query("SELECT * FROM 'edit_record'")
    fun loadAll(): List<EditRecord>

    @Query("SELECT * FROM 'edit_record' WHERE eId = :eId")
    fun queryByKey(eId: Long): EditRecord?

    @Query("SELECT * FROM 'edit_record' WHERE mId= :mId")
    fun queryByMapId(mId: Long): List<EditRecord>

}