package com.example.data.alarm.mappers

import com.example.data.alarm.entity.AlarmEntity
import com.example.domain.alarm.model.Alarm

fun Alarm.toEntity(): AlarmEntity {
    return AlarmEntity(
        id = id,
        hour = hour,
        minute = minute,
        isEnabled = isEnabled,
        repeatDays = repeatDays.joinToString(",") { it.toString() }, // Ensure correct serialization
        isRepeating = isRepeating,
        label = label,
        timestamp = timestamp,
        isVibrationEnabled = isVibrationEnabled,
        audioPath = audioPath
    )
}

fun AlarmEntity.toAlarm(): Alarm {
    return Alarm(
        id = id,
        hour = hour,
        minute = minute,
        isEnabled = isEnabled,
        repeatDays = repeatDays.split(",").mapNotNull { it.toIntOrNull() }, // Ensure correct deserialization
        isRepeating = isRepeating,
        label = label,
        timestamp = timestamp,
        isVibrationEnabled = isVibrationEnabled,
        audioPath = audioPath
    )
}