package com.example.alarm_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.core.comon.utils.Result
import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.useCases.GetAllAlarmsUseCase
import com.example.domain.alarm.useCases.UpdateAlarmUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.Calendar


class AlarmListViewModel(
    private val getAllAlarmsUseCase: GetAllAlarmsUseCase,
    private val updateAlarmUseCase: UpdateAlarmUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmListState())
    val state = _state
        .onStart {
            loadAllAlarms()  // Загружаем все алармы при старте
            if (_state.value.alarms != null) {
                startNextAlarmCountdownUpdater()
            }
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )

    fun processIntent(intent: AlarmListIntent) {
        when (intent) {
            is AlarmListIntent.LoadAllAlarms -> loadAllAlarms()
            is AlarmListIntent.OnAlarmClick -> { /* Обработка клика на аларм */
            }

            is AlarmListIntent.OnEnabledClick -> setAlarmEnabled(intent.alarmId, intent.isEnabled)
            is AlarmListIntent.CreateAlarm -> { /* Создание нового аларма */
            }
        }
    }

    private fun setAlarmEnabled(alarmId: Long, isEnabled: Boolean) {
        viewModelScope.launch {
            try {
                // Получаем текущие алармы и обновляем нужный
                val updatedAlarms = _state.value.alarms.map { alarm ->
                    if (alarm.id == alarmId) alarm.copy(isEnabled = isEnabled)
                    else alarm
                }

                // Обновляем состояние
                _state.value = _state.value.copy(alarms = updatedAlarms)

                // Получаем обновленный аларм
                val alarmUpdated = updatedAlarms.first { it.id == alarmId }

                // Обновляем данные в репозитории через use case
                when (val result = updateAlarmUseCase.invoke(alarmUpdated)) {
                    is Result.Error -> {
                        _state.update {
                            it.copy(error = result.exception.localizedMessage)
                        }
                    }

                    is Result.Success -> {
                        // Успешно обновили аларм
                    }
                }
            } catch (e: Exception) {
                // Обрабатываем исключения
                _state.update {
                    it.copy(error = e.localizedMessage)
                }
            }
        }
    }

    private fun computeNextTriggerTimeFromNow(alarms: List<Alarm>): Long? {
        val now = Calendar.getInstance()

        var closestAlarmTime: Long? = null
        var closestAlarm: Alarm? = null

        // Проходим по всем будильникам
        for (alarm in alarms) {
            val target = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, alarm.hour)
                set(Calendar.MINUTE, alarm.minute)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)
            }

            // Если время будильника уже прошло сегодня, переносим его на завтра
            if (target.timeInMillis <= now.timeInMillis) {
                target.add(Calendar.DAY_OF_YEAR, 1)
            }

            // Сравниваем, какой будильник ближайший
            if (closestAlarmTime == null || target.timeInMillis < closestAlarmTime!!) {
                closestAlarmTime = target.timeInMillis
                closestAlarm = alarm
            }
        }

        // Возвращаем время ближайшего будильника
        return closestAlarmTime
    }


    private fun startNextAlarmCountdownUpdater() {
        viewModelScope.launch {
            while (isActive) {
                val alarms = _state.value.alarms.filter { it.isEnabled }
                val now = System.currentTimeMillis()
                val nextTriggerTime = computeNextTriggerTimeFromNow(alarms)
                val description = nextTriggerTime?.let { triggerTime ->
                    val diffMillis = triggerTime - now
                    val hours = (diffMillis / (1000 * 60 * 60)).toInt()
                    val minutes = (diffMillis / (1000 * 60) % 60).toInt()
                    "Next alarm in ${hours}h ${minutes}m"
                }
                _state.update { it.copy(nextAlarmTimeDescription = description) }
                delay(60_000) // Обновлять раз в минуту
            }
        }
    }


    private fun loadAllAlarms() {
        viewModelScope.launch {
            try {
                getAllAlarmsUseCase.invoke()
                    .collect { result ->
                        when (result) {
                            is Result.Error -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        alarms = emptyList(),
                                        error = result.exception.localizedMessage
                                    )
                                }
                            }

                            is Result.Success -> {
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        alarms = result.data,
                                        error = null
                                    )
                                }
                            }
                        }
                    }
            } catch (e: Exception) {
                _state.update {
                    it.copy(error = e.localizedMessage)
                }
            }
        }
    }
}