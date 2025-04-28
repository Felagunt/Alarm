package com.example.alarmmess.di

import com.example.core.comon.utils.AudioFileProvider
import org.koin.dsl.module

val audioFileProviderModule = module {
    single { AudioFileProvider(get()) }
}