package com.example.feature.alarm.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.comon.utils.Result
import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.useCases.GetAlarmByIdUseCase
import com.example.domain.alarm.useCases.InsertAlarmUseCase
import com.example.domain.alarm.useCases.SelectAudioFileUseCase
import com.example.domain.alarm.useCases.UpdateAlarmUseCase
import com.example.feature.alarm.AlarmEditIntent
import com.example.feature.alarm.AlarmEditState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlarmEditViewModel(
    private val insertAlarmUseCase: InsertAlarmUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase,
    private val getAlarmByIdUseCase: GetAlarmByIdUseCase,
    private val selectAudioFileUseCase: SelectAudioFileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmEditState())
    val state: StateFlow<AlarmEditState> = _state

    fun processIntent(intent: AlarmEditIntent) {
        when (intent) {
            is AlarmEditIntent.LoadAlarm -> loadAlarm(intent.id)
            is AlarmEditIntent.SetTime -> updateTime(intent.hour, intent.minute)
            is AlarmEditIntent.ToggleDay -> toggleDay(intent.day)
            is AlarmEditIntent.ToggleDaily -> toggleDaily(intent.isDaily)
            is AlarmEditIntent.ToggleVibration -> toggleVibration(intent.isVibration)
            is AlarmEditIntent.SetAudioFile -> setAudioFile(intent.audioPath)
            is AlarmEditIntent.SaveAlarm -> saveAlarm()
            AlarmEditIntent.OnNavigationBack -> { }
            AlarmEditIntent.NewAlarm -> createNewAlarm()
        }
    }

    private fun createNewAlarm() {
        viewModelScope.launch {
            _state.update {
                AlarmEditState()
            }
        }
    }

    private fun setAudioFile(uri: Uri) {
        viewModelScope.launch {
            val audioFileName = selectAudioFileUseCase.execute(uri)
            _state.value = _state.value.copy(selectedAudioPath = audioFileName)
        }
    }

    private fun loadAlarm(id: Long) {
        viewModelScope.launch {
            _state.value = AlarmEditState(isLoading = true)
            when (val result = getAlarmByIdUseCase(id)) {
                is Result.Error -> _state.update { it.copy(error = result.exception.localizedMessage) }
                is Result.Success -> {
                    _state.update { it.copy(alarm = result.data) }
                }

            }
        }
    }

    private fun updateTime(hour: Int, minute: Int) {
        _state.value = _state.value.copy(selectedHour = hour, selectedMinute = minute)
    }

    private fun toggleVibration(isEnabled: Boolean) {
        _state.value = _state.value.copy(isVibrationEnabled = isEnabled)
    }

    private fun toggleDay(day: String) {
        val newSelectedDays = _state.value.selectedDays.toMutableSet().apply {
            if (contains(day)) remove(day) else add(day)
        }
        _state.value = _state.value.copy(selectedDays = newSelectedDays)
    }

    private fun toggleDaily(isDaily: Boolean) {
        if (isDaily) {
            _state.value = _state.value.copy(
                selectedDays = setOf(
                    "Mon",
                    "Tue",
                    "Wed",
                    "Thu",
                    "Fri",
                    "Sat",
                    "Sun"
                )
            )
        } else {
            _state.value = _state.value.copy(selectedDays = emptySet())
        }
        _state.value = _state.value.copy(isDailyChecked = isDaily)
    }

    private fun saveAlarm() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val alarm = Alarm(
                hour = _state.value.selectedHour,
                minute = _state.value.selectedMinute,
                isEnabled = true, // Можно добавить больше логики для этого
                repeatDays = _state.value.selectedDays.toList().map { it.toInt() },
                isRepeating = _state.value.selectedDays.isNotEmpty(),
                label = "Wake Up",
                timestamp = System.currentTimeMillis(),
                isVibrationEnabled = _state.value.isVibrationEnabled,
                audioPath = _state.value.selectedAudioPath ?: ""
            )
            if (alarm.id == -1L) {
                insertAlarmUseCase(alarm)
            } else {
                updateAlarmUseCase(alarm)
            }
            _state.value = AlarmEditState(isLoading = false, alarm = alarm)
        }
    }
}
