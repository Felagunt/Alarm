package com.example.alarm_list

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.core.comon.utils.formatTime
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
            when (intent) {
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
                ErrorScreen(
                    error = state.error,
                    modifier = Modifier.padding(paddingValues)
                ) // Show error screen
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
                    Text(
                        stringResource(R.string.no_alarms_available),
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { processIntent(AlarmListIntent.CreateAlarm) }) {
                        Text(
                            text = stringResource(R.string.create_new_alarm),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            else -> {
                // If alarms are available, show the list and the "Create" button at the bottom
                Column(modifier = Modifier.fillMaxSize()) {
                    state.nextAlarmTimeDescription?.let { description ->
                        Text(
                            text = description,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .animateContentSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(state.alarms) { alarm ->
                            AnimatedVisibility(
                                visible = true,  // Always visible, but animating fade
                                enter = fadeIn(tween(500)),
                                exit = fadeOut(tween(500))
                            ) {
                                AlarmItem(
                                    onEnabledClick = { alarmId, isEnabled ->
                                        processIntent(
                                            AlarmListIntent.OnEnabledClick(
                                                alarmId,
                                                isEnabled
                                            )
                                        )
                                    },
                                    onDeleteClick = {
                                        processIntent(AlarmListIntent.OnDeleteClick(alarm))
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
                    }
                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        onClick = { processIntent(AlarmListIntent.CreateAlarm) }
                    ) {
                        Text(
                            text = stringResource(R.string.create_new_alarm),
                            style = MaterialTheme.typography.bodyMedium
                        )
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
    onDeleteClick: () -> Unit,
    modifier: Modifier
) {
    val animatedIsEnabled by rememberUpdatedState(isEnabled)

    Card(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth()
            .animateContentSize(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.alarm_at).plus(" ${formatTime(hour, minute)}"),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            )
            Spacer(modifier = Modifier.weight(1f))
            AnimatedVisibility(
                visible = true,
                enter = fadeIn(tween(300)),
                exit = fadeOut(tween(300))
            ) {
                Switch(
                    checked = animatedIsEnabled,
                    onCheckedChange = { isChecked ->
                        onEnabledClick(alarmId, isChecked)
                    }
                )
            }
            IconButton(
                onClick = {
                    onDeleteClick.invoke()
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(R.string.delete_alarm)
                )
            }
        }
    }
}