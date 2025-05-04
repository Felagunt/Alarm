package com.example.service.alarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.core.comon.utils.Result
import com.example.domain.alarm.useCases.GetAlarmByIdUseCase
import com.example.service.alarm.service.AlarmService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.getKoin

class AlarmReceiver(
    //private val getAlarmByIdUseCase: GetAlarmByIdUseCase
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getLongExtra("ALARM_ID", -1)
        if (alarmId != -1L) {

            val getAlarmByIdUseCase: GetAlarmByIdUseCase = getKoin().get()
            // Запускаем корутину для асинхронного получения данных
            CoroutineScope(Dispatchers.IO).launch {
                // Используем GetAlarmByIdUseCase для получения данных
                val result = getAlarmByIdUseCase(alarmId)

                // Если данных нет или произошла ошибка
                if (result is Result.Success) {
                    val alarm = result.data
                    val serviceIntent = Intent(context, AlarmService::class.java).apply {
                        putExtra("ALARM_ID", alarm?.id)
                        putExtra("ALARM_VIBRATION", alarm?.isVibrationEnabled)
                        putExtra("ALARM_AUDIO_PATH", alarm?.audioPath)
                        putExtra("ALARM_REPEAT_DAYS", alarm?.repeatDays?.toIntArray())
                    }
                    // Запуск службы на переднем плане
                    ContextCompat.startForegroundService(context, serviceIntent)
                } else if (result is Result.Error) {
                    // Обработка ошибок, например, логгирование
                }
            }
        }
    }
}

