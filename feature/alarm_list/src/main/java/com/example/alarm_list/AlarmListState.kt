package com.example.alarm_list

import com.example.domain.alarm.model.Alarm

data class AlarmListState(
    val alarms: List<Alarm> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)