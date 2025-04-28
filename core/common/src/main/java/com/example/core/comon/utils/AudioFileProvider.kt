package com.example.core.comon.utils

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

class AudioFileProvider(private val context: Context) {

    fun getAudioFileName(uri: Uri): String? {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                return it.getString(columnIndex)
            }
        }
        return null
    }
}