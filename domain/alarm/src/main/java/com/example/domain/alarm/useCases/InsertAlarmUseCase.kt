package com.example.domain.alarm.useCases

import android.util.Log
import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.repository.AlarmRepository

class InsertAlarmUseCase(
    private val repository: AlarmRepository
) {
    suspend operator fun invoke(alarm: Alarm): Long
    {
        //Log.d("InsertAlarmUseCase", "Inserting alarm: $alarm")
        return repository.insertAlarm(alarm)
        //Log.d("InsertAlarmUseCase", "Alarm inserted with ID: $result")
    }
}