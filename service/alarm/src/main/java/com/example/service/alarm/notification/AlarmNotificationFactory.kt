package com.example.service.alarm.notification

import android.app.Notification

interface AlarmNotificationFactory {
    fun create(alarmId: Long): Notification
}