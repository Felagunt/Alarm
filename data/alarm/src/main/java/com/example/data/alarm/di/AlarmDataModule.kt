package com.example.data.alarm.di

import com.example.data.alarm.dao.AlarmDao
import com.example.data.alarm.dataSource.AlarmDatabase
import com.example.data.alarm.repository.AlarmRepositoryImpl
import com.example.domain.alarm.repository.AlarmRepository
import org.koin.dsl.module

val alarmDataModule = module {
    single { AlarmDatabase.getDatabase(get()) } // получаем базу данных
    single { get<AlarmDatabase>().alarmDao() }   // получаем AlarmDao

    single<AlarmRepository> { AlarmRepositoryImpl(get()) } // инжектируем Dao в репозиторий
}

