package com.example.service.alarm.player

import android.content.Context
import android.media.MediaPlayer
import android.media.Ringtone
import android.media.RingtoneManager
import android.net.Uri
import java.io.IOException

class AlarmPlayerImpl(private val context: Context) : AlarmPlayer {
    override fun play(path: String) {
        // Реализовать проигрывание аудио из указанного пути
        val mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepare()
            mediaPlayer.start()
        } catch (e: IOException) {
            e.printStackTrace()
            // Handle errors gracefully
        }
    }
    override fun playDefault() {
        try {
            val ringtoneUri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val ringtone: Ringtone = RingtoneManager.getRingtone(context, ringtoneUri)
            ringtone.play()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}