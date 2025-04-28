package com.example.domain.alarm.useCases

import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.repository.AlarmRepository

class InsertAlarmUseCase(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(alarm: Alarm) = repository.insertAlarm(alarm)
}