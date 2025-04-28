package com.example.domain.alarm.repository

import com.example.domain.alarm.model.Alarm

interface AlarmRepository {
    suspend fun insertAlarm(alarm: Alarm): Long
    suspend fun updateAlarm(alarm: Alarm)
    suspend fun deleteAlarm(alarm: Alarm)
    suspend fun getAllAlarms(): List<Alarm>
    suspend fun getAlarmById(id: Long): Alarm?
}