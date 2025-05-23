package com.example.data.alarm.repository

import android.util.Log
import com.example.data.alarm.dao.AlarmDao
import com.example.data.alarm.mappers.toAlarm
import com.example.data.alarm.mappers.toEntity
import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AlarmRepositoryImpl(
    private val alarmDao: AlarmDao
) : AlarmRepository {

    override suspend fun insertAlarm(alarm: Alarm): Long {
        //Log.d("Repository", "Alarm inserted successfully")
        return alarmDao.insert(alarm.toEntity())
    }

    override suspend fun updateAlarm(alarm: Alarm) {
        //Log.d("Repository", "Alarm updated successfully")
        alarmDao.update(alarm.toEntity())
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.delete(alarm.toEntity())
    }

    override suspend fun getAllAlarms(): Flow<List<Alarm>> {
        //Log.d("Repository", "Alarms fetched successfully")
        return alarmDao.getAllAlarms()
            .map {list ->
                list.map {entity ->
                    entity.toAlarm()
                }
        }

    }

    override suspend fun getAlarmById(id: Long): Alarm? {
        return alarmDao.getAlarmById(id)?.toAlarm()
    }
}