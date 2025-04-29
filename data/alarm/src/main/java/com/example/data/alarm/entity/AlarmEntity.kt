package com.example.data.alarm.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alarmentity")
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val hour: Int,
    val minute: Int,
    val isEnabled: Boolean,
    val repeatDays: String,
    val isRepeating: Boolean,
    val label: String,
    val timestamp: Long,
    val isVibrationEnabled: Boolean,
    val audioPath: String
)
