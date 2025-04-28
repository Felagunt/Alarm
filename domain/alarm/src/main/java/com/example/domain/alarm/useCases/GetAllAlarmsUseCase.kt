package com.example.domain.alarm.useCases

import com.example.core.comon.utils.Result
import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.repository.AlarmRepository

class GetAllAlarmsUseCase(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(): Result<List<Alarm>> {
        return try {
            val alarms = repository.getAllAlarms()
            Result.Success(alarms)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}