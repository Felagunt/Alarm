package com.example.alarmmess

import android.app.Application
import com.example.alarm_list.di.alarmListPresentationModule
import com.example.alarmmess.di.audioFileProviderModule
import com.example.alarmmess.di.databaseModule
import com.example.data.alarm.di.alarmDataModule
import com.example.domain.alarm.di.alarmDomainModule
import com.example.feature.alarm.di.alarmPresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AppAlarm: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AppAlarm)
            modules(
                listOf(
                    databaseModule,
                    alarmDataModule,
                    alarmDomainModule,
                    alarmPresentationModule,
                    alarmListPresentationModule,
                    audioFileProviderModule
                )
            )
        }
    }
}