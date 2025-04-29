package com.example.alarm_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.comon.utils.Result
import com.example.domain.alarm.useCases.GetAllAlarmsUseCase
import com.example.domain.alarm.useCases.UpdateAlarmUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
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
            loadAllAlarms()  // Загружаем все алармы при старте
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun processIntent(intent: AlarmListIntent) {
        when (intent) {
            is AlarmListIntent.LoadAllAlarms -> loadAllAlarms()
            is AlarmListIntent.OnAlarmClick -> { /* Обработка клика на аларм */ }
            is AlarmListIntent.OnEnabledClick -> setAlarmEnabled(intent.alarmId, intent.isEnabled)
            is AlarmListIntent.CreateAlarm -> { /* Создание нового аларма */ }
        }
    }

    private fun setAlarmEnabled(alarmId: Long, isEnabled: Boolean) {
        viewModelScope.launch {
            try {
                // Получаем текущие алармы и обновляем нужный
                val updatedAlarms = _state.value.alarms.map { alarm ->
                    if (alarm.id == alarmId) alarm.copy(isEnabled = isEnabled)
                    else alarm
                }

                // Обновляем состояние
                _state.value = _state.value.copy(alarms = updatedAlarms)

                // Получаем обновленный аларм
                val alarmUpdated = updatedAlarms.first { it.id == alarmId }

                // Обновляем данные в репозитории через use case
                when (val result = updateAlarmUseCase.invoke(alarmUpdated)) {
                    is Result.Error -> {
                        _state.update {
                            it.copy(error = result.exception.localizedMessage)
                        }
                    }
                    is Result.Success -> {
                        // Успешно обновили аларм
                    }
                }
            } catch (e: Exception) {
                // Обрабатываем исключения
                _state.update {
                    it.copy(error = e.localizedMessage)
                }
            }
        }
    }

    private fun loadAllAlarms() {
        viewModelScope.launch {
            try {
                getAllAlarmsUseCase.invoke()
                    .collect { result ->
                        when (result) {
                            is Result.Error -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        alarms = emptyList(),
                                        error = result.exception.localizedMessage
                                    )
                                }
                            }
                            is Result.Success -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        alarms = result.data,
                                        error = null
                                    )
                                }
                            }
                        }
                    }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.localizedMessage)
                }
            }
        }
    }
}