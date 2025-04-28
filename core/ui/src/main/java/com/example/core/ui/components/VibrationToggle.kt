package com.example.core.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun VibrationToggle(
    isVibrationEnabled: Boolean,
    onVibrationToggle: (Boolean) -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Switch(
            checked = isVibrationEnabled,
            onCheckedChange = { onVibrationToggle(it) }
        )
        Text("Vibrate")
    }
}
