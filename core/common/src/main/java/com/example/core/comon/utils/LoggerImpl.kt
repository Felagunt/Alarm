package com.example.core.comon.utils

import android.util.Log

class LoggerImpl : Logger {
    override fun d(tag: String, message: String) {
        Log.d(tag, message)
    }
}