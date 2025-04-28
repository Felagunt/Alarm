package com.example.data.alarm.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.alarm.entity.AlarmEntity

@Dao
interface AlarmDao {
    @Insert
    suspend fun insert(alarm: AlarmEntity): Long

    @Update
    suspend fun update(alarm: AlarmEntity)

    @Delete
    suspend fun delete(alarm: AlarmEntity)

    @Query("SELECT * FROM alarmentity")
    suspend fun getAllAlarms(): List<AlarmEntity>

    @Query("SELECT * FROM alarmentity WHERE id = :id")
    suspend fun getAlarmById(id: Long): AlarmEntity?
}