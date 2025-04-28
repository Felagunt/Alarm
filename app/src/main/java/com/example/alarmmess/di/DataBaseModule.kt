package com.example.alarmmess.di

import com.example.data.alarm.dataSource.AlarmDatabase
import org.koin.dsl.module

val databaseModule = module {
    single { AlarmDatabase.getDatabase(get()) } // передаем Context
    single { get<AlarmDatabase>().alarmDao() }
}
