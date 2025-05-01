package com.example.service.alarm.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.service.alarm.R
import java.io.IOException


class AlarmService : Service() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmId = intent?.getLongExtra("ALARM_ID", -1) ?: -1
        val isVibrationEnabled = intent?.getBooleanExtra("ALARM_VIBRATION", false) ?: false
        val audioPath = intent?.getStringExtra("ALARM_AUDIO_PATH") ?: ""
        val repeatDays = intent?.getIntegerArrayListExtra("ALARM_REPEAT_DAYS") ?: ArrayList()

        val notification = createNotification(alarmId)
        startForeground(1, notification)

        if (isVibrationEnabled) {
            triggerVibration(this)
        }

        if (audioPath.isNotEmpty()) {
            playAudio(audioPath)
        } else {
            // Воспроизвести стандартный звук
            playDefaultAudio()
        }

        stopSelf()
        return START_NOT_STICKY
    }

    private fun createNotification(alarmId: Long): Notification {
        val deepLinkUri = Uri.parse("app://alarm/$alarmId")
        val deepLinkIntent = Intent(Intent.ACTION_VIEW, deepLinkUri)
        val deepLinkPendingIntent = PendingIntent.getActivity(
            this,
            alarmId.toInt(),
            deepLinkIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


        val channelId = "alarm_channel"
        val channelName = "Alarms"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Alarm")
            .setContentText("Alarm fired $alarmId")
            .setContentIntent(deepLinkPendingIntent)
            .setSmallIcon(R.drawable.baseline_access_alarms_24)
            .build()
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun triggerVibration(context: Context) {
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


    private fun playAudio(audioPath: String) {
        // Реализовать проигрывание аудио из указанного пути
        val mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(audioPath)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle errors gracefully
        }
    }

    private fun playDefaultAudio() {
        try {
            val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val ringtone: Ringtone = RingtoneManager.getRingtone(applicationContext, ringtoneUri)
            ringtone.play()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


    override fun onBind(p0: Intent?): IBinder? = null
}
