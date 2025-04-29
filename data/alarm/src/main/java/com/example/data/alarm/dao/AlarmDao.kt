package com.example.data.alarm.dao

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.data.alarm.entity.AlarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {
    @Insert
    suspend fun insert(alarm: AlarmEntity): Long

    @Update
    suspend fun update(alarm: AlarmEntity)

    @Delete
    suspend fun delete(alarm: AlarmEntity)

    @Query("SELECT * FROM alarmentity")
    fun getAllAlarms(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM alarmentity WHERE id = :id")
    suspend fun getAlarmById(id: Long): AlarmEntity?
}


//@Dao
//interface AlarmDao {
//    @Insert
//    suspend fun insert(alarm: AlarmEntity) {
//        Log.d("AlarmDao", "Inserting alarm: $alarm")
//        val result = super.insert(alarm)
//        Log.d("AlarmDao", "Alarm inserted with ID: $result")
//    }
//
//    @Update
//    suspend fun update(alarm: AlarmEntity) {
//        Log.d("AlarmDao", "Updating alarm: $alarm")
//        super.update(alarm)
//        Log.d("AlarmDao", "Alarm updated successfully")
//    }
//
//    @Delete
//    suspend fun delete(alarm: AlarmEntity) {
//        Log.d("AlarmDao", "Deleting alarm: $alarm")
//        super.delete(alarm)
//        Log.d("AlarmDao", "Alarm deleted successfully")
//    }
//
//    @Query("SELECT * FROM alarmentity")
//    fun getAllAlarms(): Flow<List<AlarmEntity>> {
//        Log.d("AlarmDao", "Fetching all alarms")
//        return super.getAllAlarms().also {
//            Log.d("AlarmDao", "Alarms fetched successfully")
//        }
//    }
//
//    @Query("SELECT * FROM alarmentity WHERE id = :id")
//    suspend fun getAlarmById(id: Long): AlarmEntity? {
//        Log.d("AlarmDao", "Fetching alarm with ID: $id")
//        return super.getAlarmById(id).also {
//            Log.d("AlarmDao", "Alarm fetched successfully: $it")
//        }
//    }
//}
//
//
//
