package com.example.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.core.ui.R

@Composable
fun TimeWheelPicker(
    minValue: Int,
    maxValue: Int,
    selectedValue: Int,
    onValueChange: (Int) -> Unit,
    label: String
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        // Здесь используется Wheel Picker (например, можно использовать библиотеку для прокручиваемых списков)
        // Для простоты, будем использовать кнопки для увеличения/уменьшения значений
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { if (selectedValue > minValue) onValueChange(selectedValue - 1) }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_drop_down_24),
                    contentDescription = "Decrease"
                )
            }

            Text(
                text = "$selectedValue",
                style = MaterialTheme.typography.headlineMedium
            )

            IconButton(onClick = { if (selectedValue < maxValue) onValueChange(selectedValue + 1) }) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_arrow_drop_up_24),
                    contentDescription = "Increase"
                )
            }
        }
    }
}