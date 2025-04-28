package com.example.domain.alarm.useCases

import com.example.core.comon.utils.Result
import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.repository.AlarmRepository

class GetAlarmByIdUseCase(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(id: Long): Result<Alarm?> {
        return try {
            val alarm = repository.getAlarmById(id)
            Result.Success(alarm)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}