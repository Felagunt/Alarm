package com.example.domain.alarm.model

data class Alarm(
    val id: Long = 0,
    val hour: Int,
    val minute: Int,
    val isEnabled: Boolean,
    val repeatDays: List<Int>,
    val isRepeating: Boolean,
    val label: String,
    val timestamp: Long,
    val isVibrationEnabled: Boolean,
    val audioPath: String
)
