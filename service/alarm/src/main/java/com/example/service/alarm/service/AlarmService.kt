package com.example.service.alarm.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.service.alarm.R


class AlarmService: Service() {

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val alarmId = intent?.getLongExtra("ALARM_ID", -1) ?: -1
        val notification = createNotification(alarmId)
        startForeground(1, notification)
        //here sound, vibro etc
        stopSelf()

        return START_NOT_STICKY
    }

    private fun createNotification(alarmId: Long): Notification {

        val channelId = "alarm_channel"
        val channelName = "Alarms"

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("Alarm")
            .setContentText("Alarm fired $alarmId")
            .setSmallIcon(R.drawable.baseline_access_alarms_24)
            .build()
    }

    override fun onBind(p0: Intent?): IBinder? = null
}