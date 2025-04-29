package com.example.alarm_list

import com.example.core.comon.utils.Result
import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.useCases.GetAllAlarmsUseCase
import com.example.domain.alarm.useCases.UpdateAlarmUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AlarmListViewModelTest {

    private lateinit var alarmListViewModel: AlarmListViewModel
    private val mockGetAllAlarmsUseCase: GetAllAlarmsUseCase = mockk()
    private val mockUpdateAlarmUseCase: UpdateAlarmUseCase = mockk()

    // Create a test dispatcher for the coroutines
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        // Set the Main dispatcher to a TestCoroutineDispatcher
        Dispatchers.setMain(testDispatcher)

        alarmListViewModel = AlarmListViewModel(
            getAllAlarmsUseCase = mockGetAllAlarmsUseCase,
            updateAlarmUseCase = mockUpdateAlarmUseCase
        )
    }

    @Test
    fun `test load all alarms successfully`() = runTest {
        // Given
        val mockAlarms = listOf(
            Alarm(id = 1L, hour = 7, minute = 30, isEnabled = true, repeatDays = listOf(1), isRepeating = true, label = "Wake Up", timestamp = System.currentTimeMillis(), isVibrationEnabled = true, audioPath = "/path/to/audio"),
            Alarm(id = 2L, hour = 8, minute = 0, isEnabled = false, repeatDays = listOf(2), isRepeating = false, label = "Meeting", timestamp = System.currentTimeMillis(), isVibrationEnabled = true, audioPath = "/path/to/audio2")
        )

        // Mock the suspending function `getAllAlarmsUseCase.invoke()`
        coEvery { mockGetAllAlarmsUseCase.invoke() } returns flowOf(Result.Success(mockAlarms))

        // When
        alarmListViewModel.processIntent(AlarmListIntent.LoadAllAlarms)

        // Then
        val state = alarmListViewModel.state.first()  // Wait for the state to update
        assertEquals(state.alarms, mockAlarms)
        assertEquals(state.isLoading, false)
    }

    @Test
    fun `test set alarm enabled state successfully`() = runTest {
        // Given
        val alarmId = 1L
        val isEnabled = false
        val mockAlarms = listOf(
            Alarm(id = alarmId, hour = 7, minute = 30, isEnabled = true, repeatDays = listOf(1), isRepeating = true, label = "Wake Up", timestamp = System.currentTimeMillis(), isVibrationEnabled = true, audioPath = "/path/to/audio"),
            Alarm(id = 2L, hour = 8, minute = 0, isEnabled = false, repeatDays = listOf(2), isRepeating = false, label = "Meeting", timestamp = System.currentTimeMillis(), isVibrationEnabled = true, audioPath = "/path/to/audio2")
        )

        // Mock the suspending function `updateAlarmUseCase.invoke()`
        coEvery { mockUpdateAlarmUseCase.invoke(any()) } returns Result.Success(Unit)

        // Simulate loading all alarms and setting alarm enabled state
        alarmListViewModel.processIntent(AlarmListIntent.LoadAllAlarms)
        alarmListViewModel.processIntent(AlarmListIntent.OnEnabledClick(alarmId, isEnabled))

        // Verify that the state has been updated
        val updatedState = alarmListViewModel.state.first()

        // Ensure alarms are loaded
        assertTrue(updatedState.alarms.isNotEmpty())

        // Ensure the correct alarm is being modified
        val updatedAlarm = updatedState.alarms.firstOrNull { it.id == alarmId }
        assertNotNull(updatedAlarm)

        // Ensure the alarm's enabled state is updated correctly
        assertEquals(updatedAlarm?.isEnabled, isEnabled)

        // Verify the update function was called
        coVerify {
            if (updatedAlarm != null) {
                mockUpdateAlarmUseCase.invoke(updatedAlarm)
            }
        }
    }
}
