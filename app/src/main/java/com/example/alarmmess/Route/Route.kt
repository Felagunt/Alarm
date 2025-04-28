package com.example.alarmmess.Route

import kotlinx.serialization.Serializable

sealed interface AlarmNavGraph {

    @Serializable
    data object AlarmGraph: AlarmNavGraph

    @Serializable
    data object AlarmListScreen: AlarmNavGraph

    @Serializable
    data object AlarmCreateScreen: AlarmNavGraph

    @Serializable
    data class AlarmEditScreen(val alarmId: Long): AlarmNavGraph
}