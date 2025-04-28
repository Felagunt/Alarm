package com.example.feature.alarm

import com.example.domain.alarm.model.Alarm

data class AlarmEditState(
    val selectedHour: Int = 0,
    val selectedMinute: Int = 0,
    val selectedDays: Set<String> = emptySet(),
    val isDailyChecked: Boolean = false,
    val isVibrationEnabled: Boolean = false,
    val selectedAudioPath: String? = null,
    val alarm: Alarm? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)