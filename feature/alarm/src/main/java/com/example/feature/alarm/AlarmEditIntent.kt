package com.example.feature.alarm

import android.net.Uri

sealed class AlarmEditIntent {
    data class LoadAlarm(val id: Long) : AlarmEditIntent()
    data class SetTime(val hour: Int, val minute: Int) : AlarmEditIntent()
    data class ToggleDay(val day: String) : AlarmEditIntent()
    data class ToggleDaily(val isDaily: Boolean) : AlarmEditIntent()
    data class ToggleVibration(val isVibration: Boolean) : AlarmEditIntent()
    data class SetAudioFile(val audioPath: Uri): AlarmEditIntent()
    object SaveAlarm : AlarmEditIntent()
    object NewAlarm : AlarmEditIntent()
    object OnNavigationBack : AlarmEditIntent()
}