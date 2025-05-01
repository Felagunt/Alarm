package com.example.feature.alarm.viewModel

import android.net.Uri
import android.util.Log
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
import java.util.Calendar

class AlarmEditViewModel(
    private val insertAlarmUseCase: InsertAlarmUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase,
    private val getAlarmByIdUseCase: GetAlarmByIdUseCase,
    private val selectAudioFileUseCase: SelectAudioFileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmEditState())
    val state: StateFlow<AlarmEditState> = _state

    fun processIntent(intent: AlarmEditIntent) {
        //Log.d("AlarmEditViewModel", "Processing intent: $intent")
        when (intent) {
            is AlarmEditIntent.LoadAlarm -> loadAlarm(intent.id)
            is AlarmEditIntent.SetTime -> updateTime(intent.hour, intent.minute)
            is AlarmEditIntent.ToggleDay -> toggleDay(intent.day)
            is AlarmEditIntent.ToggleDaily -> toggleDaily(intent.isDaily)
            is AlarmEditIntent.ToggleVibration -> toggleVibration(intent.isVibration)
            is AlarmEditIntent.SetAudioFile -> setAudioFile(intent.audioPath)
            is AlarmEditIntent.SaveAlarm -> saveAlarm()
            is AlarmEditIntent.OnNavigationBack ->{}// Log.d("AlarmEditViewModel", "Handling navigation back")
            is AlarmEditIntent.NewAlarm -> createNewAlarm()
        }
    }

    private fun createNewAlarm() {
        val now = Calendar.getInstance()
        val currentHour = now.get(Calendar.HOUR_OF_DAY)
        val currentMinute = now.get(Calendar.MINUTE)

        _state.update {
            AlarmEditState(
                alarm = Alarm(
                    id = 0L,
                    hour = currentHour,
                    minute = currentMinute,
                    isEnabled = true,
                    repeatDays = emptyList(), // по умолчанию не повторяется
                    isRepeating = false,
                    label = "Wake Up",
                    timestamp = System.currentTimeMillis(),
                    isVibrationEnabled = false,
                    audioPath = ""
                ),
                selectedHour = currentHour,
                selectedMinute = currentMinute
            )
        }
    }


    private fun setAudioFile(uri: Uri) {
        viewModelScope.launch {
            try {
                val audioFileName = selectAudioFileUseCase.execute(uri)
                _state.value = _state.value.copy(selectedAudioPath = audioFileName)
            } catch (e: Exception) {
                _state.value = _state.value.copy(error = "Audio file not found.")
            }
        }
    }

    private fun loadAlarm(id: Long) {
        viewModelScope.launch {
            _state.value = AlarmEditState(isLoading = true)
            when (val result = getAlarmByIdUseCase(id)) {
                is Result.Error -> {
                    _state.update { it.copy(
                        error = result.exception.localizedMessage,
                        isLoading = false,
                        alarm = null
                    ) }
                }
                is Result.Success -> {
                    _state.update { it.copy(
                        alarm = result.data,
                        selectedHour = result.data!!.hour,
                        selectedMinute = result.data!!.minute,
                        selectedDays = result.data!!.repeatDays.mapNotNull {
                            when (it) {
                                1 -> "Mon"
                                2 -> "Tue"
                                3 -> "Wed"
                                4 -> "Thu"
                                5 -> "Fri"
                                6 -> "Sat"
                                7 -> "Sun"
                                else -> null
                            }
                        }.toSet(),
                        isDailyChecked = result.data!!.repeatDays.size == 7,
                        isVibrationEnabled = result.data!!.isVibrationEnabled,
                        selectedAudioPath = result.data!!.audioPath,
                        isLoading = false
                    ) }
                }
            }
        }
    }

    private fun updateTime(hour: Int, minute: Int) {
        //Log.d("AlarmEditViewModel", "Updating time to Hour: $hour, Minute: $minute")
        _state.value = _state.value.copy(selectedHour = hour, selectedMinute = minute)
    }

    private fun toggleVibration(isEnabled: Boolean) {
        //Log.d("AlarmEditViewModel", "Toggling vibration to: $isEnabled")
        _state.value = _state.value.copy(isVibrationEnabled = isEnabled)
    }

    private fun toggleDay(day: String) {
        //Log.d("AlarmEditViewModel", "Toggling day: $day")
        val newSelectedDays = _state.value.selectedDays.toMutableSet().apply {
            if (contains(day)) remove(day) else add(day)
        }
        _state.value = _state.value.copy(selectedDays = newSelectedDays)
        //Log.d("AlarmEditViewModel", "Updated selected days: ${_state.value.selectedDays}")
    }

    private fun toggleDaily(isDaily: Boolean) {
        //Log.d("AlarmEditViewModel", "Toggling daily to: $isDaily")
        if (isDaily) {
            _state.value = _state.value.copy(
                selectedDays = setOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            )
        } else {
            _state.value = _state.value.copy(selectedDays = emptySet())
        }
        _state.value = _state.value.copy(isDailyChecked = isDaily)
        //Log.d("AlarmEditViewModel", "Daily toggle updated. Selected days: ${_state.value.selectedDays}")
    }

    private fun saveAlarm() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            // Construct the alarm from the current state
            val alarm = createAlarmFromState()

            // Save or update alarm
            if (alarm.id == 0L) {
                // Insert a new alarm
                insertAlarmUseCase(alarm)
            } else {
                // Update the existing alarm
                updateAlarmUseCase(alarm)
            }

            _state.value = _state.value.copy(isLoading = false, alarm = alarm)
        }
    }

    private fun createAlarmFromState(): Alarm {
        val dayMapping = mapOf(
            "Mon" to 1,
            "Tue" to 2,
            "Wed" to 3,
            "Thu" to 4,
            "Fri" to 5,
            "Sat" to 6,
            "Sun" to 7
        )

        val repeatDaysAsIntegers = _state.value.selectedDays.mapNotNull { dayMapping[it] }

        return Alarm(
            id = _state.value.alarm?.id ?: 0L,  // Use current alarm ID or default to -1
            hour = _state.value.selectedHour,
            minute = _state.value.selectedMinute,
            isEnabled = true,
            repeatDays = repeatDaysAsIntegers,
            isRepeating = repeatDaysAsIntegers.isNotEmpty(),
            label = _state.value.alarm?.label ?: "Wake Up",
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = _state.value.isVibrationEnabled,
            audioPath = _state.value.selectedAudioPath ?: ""
        )
    }
}