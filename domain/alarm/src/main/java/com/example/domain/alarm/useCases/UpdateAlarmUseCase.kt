package com.example.domain.alarm.useCases

import com.example.core.comon.utils.Result
import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.repository.AlarmRepository

class UpdateAlarmUseCase(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(alarm: Alarm): Result<Unit> {
        return try {
            repository.updateAlarm(alarm)
            Result.Success(Unit)
        } catch (e:Exception) {
            Result.Error(e)
        }
    }
}