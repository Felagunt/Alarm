package com.example.alarm_list.di

import com.example.alarm_list.AlarmListViewModel
import com.example.domain.alarm.useCases.GetAllAlarmsUseCase
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val alarmListPresentationModule = module {
    viewModel { AlarmListViewModel(get(), get()) } //
}