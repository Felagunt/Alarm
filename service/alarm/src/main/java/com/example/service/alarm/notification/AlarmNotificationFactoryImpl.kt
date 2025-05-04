package com.example.service.alarm.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.service.alarm.R

class AlarmNotificationFactoryImpl(private val context: Context) : AlarmNotificationFactory {
    override fun create(alarmId: Long): Notification {
        val deepLinkUri = Uri.parse("app://alarm/$alarmId")
        val deepLinkIntent = Intent(Intent.ACTION_VIEW, deepLinkUri)
        val deepLinkPendingIntent = PendingIntent.getActivity(
            context,
            alarmId.toInt(),
            deepLinkIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


        val channelId = "alarm_channel"
        val channelName = "Alarms"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
        return NotificationCompat.Builder(context, channelId)
            .setContentTitle("Alarm")
            .setContentText("Alarm fired $alarmId")
            .setContentIntent(deepLinkPendingIntent)
            .setSmallIcon(R.drawable.baseline_access_alarms_24)
            .build()
    }
}