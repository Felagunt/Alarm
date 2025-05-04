package com.example.alarm_list.di

import com.example.alarm_list.AlarmListViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val alarmListPresentationModule = module {
    viewModel {
        AlarmListViewModel(
            getAllAlarmsUseCase = get(),
            updateAlarmUseCase = get(),
            deleteAlarmUseCase = get()
        )
    } //
}