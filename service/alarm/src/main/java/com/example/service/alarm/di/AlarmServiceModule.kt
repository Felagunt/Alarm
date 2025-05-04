package com.example.service.alarm.di

import com.example.service.alarm.notification.AlarmNotificationFactory
import com.example.service.alarm.notification.AlarmNotificationFactoryImpl
import com.example.service.alarm.player.AlarmPlayer
import com.example.service.alarm.player.AlarmPlayerImpl
import com.example.service.alarm.vibration.VibrationHandler
import com.example.service.alarm.vibration.VibrationHandlerImpl
import org.koin.dsl.module

val alarmServiceModule = module {
    single<AlarmPlayer> { AlarmPlayerImpl(get()) }
    single<VibrationHandler> { VibrationHandlerImpl(get()) }
    single<AlarmNotificationFactory> { AlarmNotificationFactoryImpl(get()) }
}