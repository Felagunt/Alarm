package com.example.data.alarm.mappers

import com.example.data.alarm.entity.AlarmEntity
import com.example.domain.alarm.model.Alarm

fun Alarm.toEntity(): AlarmEntity {
    val repeatDaysString = repeatDays.joinToString(",")
    return AlarmEntity(
        id = id,
        hour = hour,
        minute = minute,
        isEnabled = isEnabled,
        repeatDays = repeatDaysString,
        isRepeating = isRepeating,
        label = label,
        timestamp = timestamp,
        isVibrationEnabled = isVibrationEnabled,
        audioPath = audioPath
    )
}

fun AlarmEntity.toAlarm(): Alarm {
    val repeatDaysList = repeatDays.split(",").map { it.toInt() }
    return Alarm(
        id = id,
        hour = hour,
        minute = minute,
        isEnabled = isEnabled,
        repeatDays = repeatDaysList,
        isRepeating = isRepeating,
        label = label,
        timestamp = timestamp,
        isVibrationEnabled = isVibrationEnabled,
        audioPath = audioPath
    )
}