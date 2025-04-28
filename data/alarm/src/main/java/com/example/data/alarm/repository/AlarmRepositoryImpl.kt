package com.example.data.alarm.repository

import com.example.data.alarm.dao.AlarmDao
import com.example.data.alarm.mappers.toAlarm
import com.example.data.alarm.mappers.toEntity
import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.repository.AlarmRepository

class AlarmRepositoryImpl(
    private val alarmDao: AlarmDao
) : AlarmRepository {

    override suspend fun insertAlarm(alarm: Alarm): Long {
        return alarmDao.insert(alarm.toEntity())
    }

    override suspend fun updateAlarm(alarm: Alarm) {
        alarmDao.update(alarm.toEntity())
    }

    override suspend fun deleteAlarm(alarm: Alarm) {
        alarmDao.delete(alarm.toEntity())
    }

    override suspend fun getAllAlarms(): List<Alarm> {
        return alarmDao.getAllAlarms()
            .map {
                it.toAlarm()
            }
    }

    override suspend fun getAlarmById(id: Long): Alarm? {
        return alarmDao.getAlarmById(id)?.toAlarm()
    }
}