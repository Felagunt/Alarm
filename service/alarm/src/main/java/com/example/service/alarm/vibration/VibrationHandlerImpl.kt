package com.example.service.alarm.vibration

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.VibratorManager
import androidx.annotation.RequiresApi

class VibrationHandlerImpl(private val context: Context) : VibrationHandler {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun vibrate() {
        // Проверяем, поддерживает ли устройство вибрацию
        val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        val vibrator = vibratorManager.defaultVibrator

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val vibrationEffect = VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
        } else {
            vibrator.vibrate(1000)  // Для старых версий
        }
    }
}