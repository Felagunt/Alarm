package com.example.feature.alarm.di

import com.example.feature.alarm.viewModel.AlarmEditViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val alarmPresentationModule = module {
    viewModel {
        AlarmEditViewModel(
            insertAlarmUseCase = get(),
            updateAlarmUseCase = get(),
            getAlarmByIdUseCase = get(),
            selectAudioFileUseCase = get()
        )
    }
}
