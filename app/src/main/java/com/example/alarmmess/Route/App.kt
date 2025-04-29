package com.example.alarmmess.Route

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController

@Composable
fun App(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    AlarmNavGraph(
        modifier = modifier,
        navController = navController
    )
}