package com.example.feature.alarm

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.ui.components.ErrorScreen
import com.example.core.ui.components.LoadingScreen
import com.example.core.ui.components.TimePicker
import com.example.core.ui.components.VibrationToggle
import com.example.core.ui.components.WeekdaySelector
import com.example.feature.alarm.viewModel.AlarmEditViewModel
import org.koin.androidx.compose.koinViewModel


@Composable
fun AlarmEditScreenRoot(
    alarmId: Long?,
    viewModel: AlarmEditViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(alarmId) {
        when (alarmId) {
            null -> viewModel.processIntent(AlarmEditIntent.NewAlarm)
            else -> viewModel.processIntent(AlarmEditIntent.LoadAlarm(alarmId))
        }
    }

    val title = if (alarmId == null) "Create Alarm" else "Edit Alarm"

    AlarmEditScreen(
        state = state,
        processIntent = { intent ->
            when (intent) {
                is AlarmEditIntent.OnNavigationBack -> {
                    onBackClick()
                }

                else -> Unit
            }
            viewModel.processIntent(intent)
        },
        title = title
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AlarmEditScreen(
    state: AlarmEditState,
    processIntent: (AlarmEditIntent) -> Unit,
    title: String,
) {
    val audioPickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            uri?.let {
                processIntent(AlarmEditIntent.SetAudioFile(it))
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = {
                    IconButton(onClick = { processIntent(AlarmEditIntent.OnNavigationBack) }) {
                        Icon(
                            Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Navigate Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.error != null) {
            ErrorScreen(modifier = Modifier.padding(paddingValues), error = state.error)
        } else if (state.isLoading) {
            LoadingScreen(modifier = Modifier.padding(paddingValues))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Time
                TimePicker(
                    selectedHour = state.selectedHour,
                    selectedMinute = state.selectedMinute,
                    onHourChange = {
                        processIntent(
                            AlarmEditIntent.SetTime(
                                it,
                                state.selectedMinute
                            )
                        )
                    },
                    onMinuteChange = {
                        processIntent(
                            AlarmEditIntent.SetTime(
                                state.selectedHour,
                                it
                            )
                        )
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Weekdays
                WeekdaySelector(
                    selectedDays = state.selectedDays,
                    onDaySelected = { day ->
                        processIntent(AlarmEditIntent.ToggleDay(day))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Daily Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = state.isDailyChecked,
                        onCheckedChange = {
                            processIntent(AlarmEditIntent.ToggleDaily(it))
                        }
                    )
                    Text("Everyday")
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(onClick = {
                        audioPickerLauncher.launch(arrayOf("audio/"))
                        /* Launch intent to select audio file */

                    }) {
                        Text("Select Alarm Sound")
                    }

                    if (state.selectedAudioPath != null) {
                        Text("Selected Audio: ${state.selectedAudioPath}")
                    }

                    VibrationToggle(
                        onVibrationToggle = {
                            processIntent(AlarmEditIntent.ToggleVibration(it))
                        },
                        isVibrationEnabled = state.isVibrationEnabled
                    )


                }

                Spacer(modifier = Modifier.height(16.dp))

                // Save Button
                Button(
                    onClick = {
                        processIntent(AlarmEditIntent.SaveAlarm)
                        processIntent(AlarmEditIntent.OnNavigationBack)
                    }
                ) {
                    Text("Save Alarm")
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewAlarmEditScreen() {
    AlarmEditScreen(
        state = AlarmEditState(),
        processIntent = {},
        title = TODO()
    )
}
