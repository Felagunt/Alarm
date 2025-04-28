package com.example.alarm_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.ui.components.ErrorScreen
import com.example.core.ui.components.LoadingScreen
import org.koin.androidx.compose.koinViewModel

@Composable
fun AlarmListScreenRoot(
    viewModel: AlarmListViewModel = koinViewModel(),
    onAlarmClick: (Long) -> Unit,
    onCreateAlarmClick: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    AlarmListScreen(
        state = state,
        processIntent = { intent ->
            when(intent) {
                is AlarmListIntent.OnAlarmClick -> onAlarmClick(intent.alarmId)
                is AlarmListIntent.CreateAlarm -> onCreateAlarmClick()
                else -> Unit
            }
            viewModel.processIntent(intent)
        }
    )

}

@Composable
private fun AlarmListScreen(
    state: AlarmListState,
    processIntent: (AlarmListIntent) -> Unit
) {

    Scaffold { paddingValues ->
        if (state.isLoading) {
            LoadingScreen(modifier = Modifier.padding(paddingValues)) // Показываем экран загрузки
        } else if (state.error != null) {
            ErrorScreen(error = state.error, modifier = Modifier.padding(paddingValues)) // Показываем ошибку, если она есть
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.alarms) { alarm ->
                    AlarmItem(
                        onEnabledClick = { alarmId, isEnabled ->
                            processIntent(AlarmListIntent.OnEnabledClick(alarmId, isEnabled))
                        },
                        hour = alarm.hour,
                        minute = alarm.minute,
                        isEnabled = alarm.isEnabled,
                        alarmId = alarm.id,
                        modifier = Modifier
                            .clickable {
                                processIntent(AlarmListIntent.OnAlarmClick(alarm.id))
                            }
                    )
                }
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(paddingValues),
                onClick = {
                    processIntent(AlarmListIntent.CreateAlarm)
                }
            ) {
                Text(
                    text = stringResource(R.string.create_new_alarm),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

    }
}



@Composable
fun AlarmItem(
    hour: Int,
    minute: Int,
    isEnabled: Boolean,
    alarmId: Long,
    onEnabledClick: (Long, Boolean) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Alarm at ${hour}:${minute}",
            style = MaterialTheme.typography.bodySmall
        )

        Switch(
            checked = isEnabled,
            onCheckedChange = { isChecked ->
                onEnabledClick(alarmId, isChecked) // Вызов функции для изменения состояния
            }
        )
    }
}