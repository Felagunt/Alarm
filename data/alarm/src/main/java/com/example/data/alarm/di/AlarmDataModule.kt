package com.example.data.alarm.di

import com.example.data.alarm.repository.AlarmRepositoryImpl
import com.example.domain.alarm.repository.AlarmRepository
import org.koin.dsl.module

val alarmDataModule = module {
    single<AlarmRepository> { AlarmRepositoryImpl(get()) } // инжектируем Dao в репозиторий
}

