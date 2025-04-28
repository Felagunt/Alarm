package com.example.alarm_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
        when {
            state.isLoading -> {
                LoadingScreen(modifier = Modifier.padding(paddingValues)) // Show loading screen
            }
            state.error != null -> {
                ErrorScreen(error = state.error, modifier = Modifier.padding(paddingValues)) // Show error screen
            }
            state.alarms.isEmpty() -> {
                // If there are no alarms, show a message and the "Create" button
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("No alarms available.")
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { processIntent(AlarmListIntent.CreateAlarm) }) {
                        Text("Create New Alarm")
                    }
                }
            }
            else -> {
                // If alarms are available, show the list and the "Create" button at the bottom
                Column(modifier = Modifier.fillMaxSize()) {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
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
                            .padding(16.dp),
                        onClick = { processIntent(AlarmListIntent.CreateAlarm) }
                    ) {
                        Text("Create New Alarm")
                    }
                }
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