package com.example.alarm_list

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.comon.utils.Result
import com.example.domain.alarm.useCases.GetAllAlarmsUseCase
import com.example.domain.alarm.useCases.UpdateAlarmUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlarmListViewModel(
    private val getAllAlarmsUseCase: GetAllAlarmsUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmListState())
    val state = _state
        .onStart {
            Log.d("AlarmListViewModel", "State flow started, loading all alarms")
            loadAllAlarms()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun processIntent(intent: AlarmListIntent) {
        Log.d("AlarmListViewModel", "Processing intent: $intent")
        when (intent) {
            is AlarmListIntent.LoadAllAlarms -> loadAllAlarms()
            is AlarmListIntent.OnAlarmClick -> Log.d(
                "AlarmListViewModel",
                "Alarm clicked with ID: ${intent.alarmId}"
            )

            is AlarmListIntent.OnEnabledClick -> setAlarmEnabled(intent.alarmId, intent.isEnabled)
            is AlarmListIntent.CreateAlarm -> Log.d(
                "AlarmListViewModel",
                "Create Alarm intent received"
            )
        }
    }

    private fun setAlarmEnabled(alarmId: Long, isEnabled: Boolean) {
        Log.d(
            "AlarmListViewModel",
            "Setting alarm enabled state. ID: $alarmId, isEnabled: $isEnabled"
        )
        viewModelScope.launch {
            try {
                val updatedAlarms = _state.value.alarms.map { alarm ->
                    if (alarm.id == alarmId) {
                        alarm.copy(isEnabled = isEnabled)
                    } else {
                        alarm
                    }
                }
                _state.value = _state.value.copy(alarms = updatedAlarms)
                Log.d("AlarmListViewModel", "Updated alarms: $updatedAlarms")

                val alarmUpdated = updatedAlarms.first { it.id == alarmId }
                when (val result = updateAlarmUseCase.invoke(alarmUpdated)) {
                    is Result.Error -> {
                        Log.e("AlarmListViewModel", "Error updating alarm: ${result.exception}")
                        _state.update { it.copy(error = result.exception.localizedMessage) }
                    }

                    is Result.Success -> {
                        Log.d("AlarmListViewModel", "Alarm updated successfully")
                    }
                }
            } catch (e: Exception) {
                Log.e("AlarmListViewModel", "Error setting alarm enabled state: $e")
            }
        }
    }

    private fun loadAllAlarms() {
        Log.d("AlarmListViewModel", "Loading all alarms")
        try {
            getAllAlarmsUseCase.invoke()
                .onEach { result ->
                    Log.d("AlarmListViewModel", "Received result: $result") // Логируем результат
                    when (result) {
                        is Result.Error -> {
                            Log.e("AlarmListViewModel", "Error loading alarms: ${result.exception}")
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    alarms = emptyList(),
                                    error = result.exception.localizedMessage
                                )
                            }
                        }

                        is Result.Success -> {
                            Log.d("AlarmListViewModel", "Loaded alarms successfully: ${result.data}")
                            _state.update {
                                it.copy(
                                    error = null,
                                    isLoading = false,
                                    alarms = result.data // Обновляем список алармов
                                )
                            }
                        }
                    }
                }
                .launchIn(viewModelScope) // Запуск асинхронной операции
        } catch (e: Exception) {
            Log.e("AlarmListViewModel", "Error in loadAllAlarms: $e")
        }
    }


}