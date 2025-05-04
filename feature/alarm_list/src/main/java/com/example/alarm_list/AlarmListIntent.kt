package com.example.alarm_list

import com.example.domain.alarm.model.Alarm

sealed class AlarmListIntent {
    object LoadAllAlarms : AlarmListIntent()
    object CreateAlarm : AlarmListIntent()
    data class OnAlarmClick(val alarmId: Long): AlarmListIntent()
    data class OnDeleteClick(val alarm: Alarm): AlarmListIntent()
    data class OnEnabledClick(val alarmId: Long, val isEnabled: Boolean): AlarmListIntent()
}