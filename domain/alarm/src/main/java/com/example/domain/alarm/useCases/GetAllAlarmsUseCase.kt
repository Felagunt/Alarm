package com.example.domain.alarm.useCases

import android.util.Log
import com.example.core.comon.utils.Result
import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetAllAlarmsUseCase(private val repository: AlarmRepository) {
    operator fun invoke(): Flow<Result<List<Alarm>>> = flow {
        try {
            repository.getAllAlarms().collect { list -> // Вы подписываетесь на поток здесь
                //Log.d("GetAllAlarmsUseCase", "Emitting alarms: $list")
                emit(Result.Success(list))
            }
        } catch (e: Exception) {
            //Log.e("GetAllAlarmsUseCase", "Error fetching alarms: ${e.localizedMessage}")
            emit(Result.Error(e))
        }
    }
}
