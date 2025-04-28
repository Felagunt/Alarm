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
            loadAllAlarms()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun processIntent(intent: AlarmListIntent) {
        when (intent) {
            is AlarmListIntent.LoadAllAlarms -> loadAllAlarms()
            is AlarmListIntent.OnAlarmClick -> { }
            is AlarmListIntent.OnEnabledClick -> setAlarmEnabled(intent.alarmId,intent.isEnabled)
            is AlarmListIntent.CreateAlarm -> { }
        }
    }

    private fun setAlarmEnabled(alarmId: Long,isEnabled: Boolean) {
        viewModelScope.launch {
            val updatedAlarms = _state.value.alarms.map { alarm ->
                if(alarm.id == alarmId) {
                    alarm.copy(isEnabled = isEnabled)
                } else {
                    alarm
                }
            }
            _state.value = _state.value.copy(alarms = updatedAlarms)
            val alarmUpdated = updatedAlarms.first {it.id == alarmId}
            when(val result = updateAlarmUseCase.invoke(alarmUpdated)) {
                is Result.Error -> _state.update {it.copy(error = result.exception.localizedMessage)}
                is Result.Success ->{ }
            }
        }
    }

    private fun loadAllAlarms() {
        viewModelScope.launch {
            _state.value = AlarmListState(isLoading = true)
            when(val result = getAllAlarmsUseCase.invoke()) {
                is Result.Error -> {
                    _state.update {
                        it.copy(
                            error = result.exception.localizedMessage
                        )
                    }
                }
                is Result.Success -> {
                    _state.update {
                        it.copy(
                            error = null,
                            isLoading = false,
                            alarms = result.data
                        )
                    }
                }
            }
        }
    }
}