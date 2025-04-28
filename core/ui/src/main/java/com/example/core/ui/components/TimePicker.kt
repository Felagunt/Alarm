package com.example.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun TimePicker(
    selectedHour: Int,
    selectedMinute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit
) {
    Column {
        Text("Time", style = MaterialTheme.typography.headlineMedium)

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeWheelPicker(
                minValue = 0,
                maxValue = 23,
                selectedValue = selectedHour,
                onValueChange = onHourChange,
                label = "Hours"
            )

            Spacer(modifier = Modifier.width(16.dp))

            TimeWheelPicker(
                minValue = 0,
                maxValue = 59,
                selectedValue = selectedMinute,
                onValueChange = onMinuteChange,
                label = "Minutes"
            )
        }
    }
}