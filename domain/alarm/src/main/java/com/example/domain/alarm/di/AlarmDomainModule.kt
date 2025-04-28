package com.example.domain.alarm.di

import com.example.domain.alarm.useCases.DeleteAlarmUseCase
import com.example.domain.alarm.useCases.GetAlarmByIdUseCase
import com.example.domain.alarm.useCases.GetAllAlarmsUseCase
import com.example.domain.alarm.useCases.InsertAlarmUseCase
import com.example.domain.alarm.useCases.SelectAudioFileUseCase
import com.example.domain.alarm.useCases.UpdateAlarmUseCase
import org.koin.dsl.module

val alarmDomainModule = module {
    factory { InsertAlarmUseCase(get()) }
    factory { UpdateAlarmUseCase(get()) }
    factory { DeleteAlarmUseCase(get()) }
    factory { GetAlarmByIdUseCase(get()) }
    factory { GetAllAlarmsUseCase(get()) }
    factory { SelectAudioFileUseCase(get()) }
}
