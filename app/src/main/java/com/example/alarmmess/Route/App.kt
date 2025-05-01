package com.example.alarmmess.Route

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.rememberNavController

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    // Обработка Deep Link
    val context = LocalContext.current
    LaunchedEffect(context) {
        val intent = (context as? Activity)?.intent
        intent?.data?.let { uri ->
            val alarmId = uri.lastPathSegment?.toLongOrNull()
            if (alarmId != null) {
                //navController.navigate("alarm/$alarmId")
                navController.navigate(AlarmNavGraph.AlarmFiredScreen(alarmId))
            }
        }
    }

    AlarmNavGraph(
        modifier = modifier,
        navController = navController
    )
}