package com.example.alarmmess.Route

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.example.alarm_list.AlarmListScreenRoot
import com.example.alarm_list.AlarmListViewModel
import com.example.feature.alarm.AlarmEditScreenRoot
import com.example.feature.alarm.viewModel.AlarmEditViewModel
import com.example.fired_alarm.AlarmFiredScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AlarmNavGraph(modifier: Modifier = Modifier, navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = AlarmNavGraph.AlarmGraph,
        exitTransition = composable@{
            return@composable slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(700)
            )
        },
        popEnterTransition = composable@{
            return@composable slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(700)
            )
        }
    ) {
        navigation<AlarmNavGraph.AlarmGraph>(
            startDestination = AlarmNavGraph.AlarmListScreen
        ) {
            composable<AlarmNavGraph.AlarmListScreen>() {
                val viewModel = koinViewModel<AlarmListViewModel>()
                AlarmListScreenRoot(
                    viewModel = viewModel,
                    onAlarmClick = { alarmId ->
                        navController.navigate(AlarmNavGraph.AlarmEditScreen(alarmId = alarmId))
                    },
                    onCreateAlarmClick = {
                        navController.navigate(AlarmNavGraph.AlarmCreateScreen)
                    }
                )
            }

            composable<AlarmNavGraph.AlarmCreateScreen> {
                val viewModel = koinViewModel<AlarmEditViewModel>()
                AlarmEditScreenRoot(
                    viewModel = viewModel,
                    onBackClick = { navController.navigateUp() },
                    alarmId = null
                )
            }

            composable<AlarmNavGraph.AlarmEditScreen> {
                val args = it.toRoute<AlarmNavGraph.AlarmEditScreen>()
                val viewModel = koinViewModel<AlarmEditViewModel>()
                AlarmEditScreenRoot(
                    viewModel = viewModel,
                    onBackClick = {
                        navController.navigateUp()
                    },
                    alarmId = args.alarmId
                )
            }

            composable<AlarmNavGraph.AlarmFiredScreen> {
                val alarmId = it.toRoute<AlarmNavGraph.AlarmFiredScreen>().alarmId
                //val alarmId = it.arguments?.getLong("alarmId") ?: return@composable
                AlarmFiredScreen(
                    alarmId = alarmId
                )
            }
        }
    }
}