package com.example.domain.alarm.useCases

import android.net.Uri
import com.example.core.comon.utils.AudioFileProvider

class SelectAudioFileUseCase(
    private val audioFileProvider: AudioFileProvider
) {

    fun execute(uri: Uri): String? {
        return audioFileProvider.getAudioFileName(uri)
    }
}