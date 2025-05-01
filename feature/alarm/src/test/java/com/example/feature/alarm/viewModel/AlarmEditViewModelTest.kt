package com.example.feature.alarm.viewModel
import android.net.Uri
import com.example.core.comon.utils.Result
import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.useCases.GetAlarmByIdUseCase
import com.example.domain.alarm.useCases.InsertAlarmUseCase
import com.example.domain.alarm.useCases.SelectAudioFileUseCase
import com.example.domain.alarm.useCases.UpdateAlarmUseCase
import com.example.feature.alarm.AlarmEditIntent
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class AlarmEditViewModelTest {

    private lateinit var alarmEditViewModel: AlarmEditViewModel

    private val insertAlarmUseCase: InsertAlarmUseCase = mockk(relaxed = true)
    private val updateAlarmUseCase: UpdateAlarmUseCase = mockk(relaxed = true)
    private val getAlarmByIdUseCase: GetAlarmByIdUseCase = mockk(relaxed = true)
    private val selectAudioFileUseCase: SelectAudioFileUseCase = mockk(relaxed = true)

    private val testDispatcher = UnconfinedTestDispatcher() // Use UnconfinedTestDispatcher
    private val testScope = TestScope(testDispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        alarmEditViewModel = AlarmEditViewModel(
            insertAlarmUseCase,
            updateAlarmUseCase,
            getAlarmByIdUseCase,
            selectAudioFileUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load alarm successfully`() = testScope.runTest {
        // Given
        val alarmId = 1L
        val alarm = Alarm(
            id = alarmId,
            hour = 7,
            minute = 30,
            isEnabled = true,
            repeatDays = listOf(1),
            isRepeating = true,
            label = "Wake Up",
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = true,
            audioPath = "/path/to/audio"
        )

        coEvery { getAlarmByIdUseCase(alarmId) } returns Result.Success(alarm)

        // When
        alarmEditViewModel.processIntent(AlarmEditIntent.LoadAlarm(alarmId))

        // Then
        val state = alarmEditViewModel.state.first()
        assertNotNull(state.alarm)
        assertEquals(alarmId, state.alarm?.id)
        assertEquals(alarm.hour, state.alarm?.hour)
        assertEquals(alarm.minute, state.alarm?.minute)
        assertFalse(state.isLoading)
        assertNull(state.error)
    }

    @Test
    fun `load alarm failure`() = testScope.runTest {
        // Given
        val alarmId = 1L
        val errorMessage = "Failed to load alarm"
        coEvery { getAlarmByIdUseCase(alarmId) } returns Result.Error(Exception(errorMessage))

        // When
        alarmEditViewModel.processIntent(AlarmEditIntent.LoadAlarm(alarmId))

        // Then
        val state = alarmEditViewModel.state.first()
        assertNull(state.alarm)
        assertTrue(state.isLoading)
        assertEquals(errorMessage, state.error)
    }

    @Test
    fun `set time`() = testScope.runTest {
        // Given
        val hour = 8
        val minute = 45

        // When
        alarmEditViewModel.processIntent(AlarmEditIntent.SetTime(hour, minute))

        // Then
        val state = alarmEditViewModel.state.first()
        assertEquals(hour, state.selectedHour)
        assertEquals(minute, state.selectedMinute)
    }

    @Test
    fun `toggle vibration`() = testScope.runTest {
        // Given
        val isVibrationEnabled = true

        // When
        alarmEditViewModel.processIntent(AlarmEditIntent.ToggleVibration(isVibrationEnabled))

        // Then
        val state = alarmEditViewModel.state.first()
        assertEquals(isVibrationEnabled, state.isVibrationEnabled)
    }

    @Test
    fun `toggle day - add day`() = testScope.runTest {
        // Given
        val day = "Mon"

        // When
        alarmEditViewModel.processIntent(AlarmEditIntent.ToggleDay(day))

        // Then
        val state = alarmEditViewModel.state.first()
        assertEquals(setOf(day), state.selectedDays)
    }

    @Test
    fun `toggle day - remove day`() = testScope.runTest {
        // Given
        val day = "Mon"
        alarmEditViewModel.processIntent(AlarmEditIntent.ToggleDay(day)) // Add the day first

        // When
        alarmEditViewModel.processIntent(AlarmEditIntent.ToggleDay(day)) // Toggle it again

        // Then
        val state = alarmEditViewModel.state.first()
        assertTrue(state.selectedDays.isEmpty())
    }

    @Test
    fun `toggle daily - enable daily`() = testScope.runTest {
        // Given
        val isDaily = true

        // When
        alarmEditViewModel.processIntent(AlarmEditIntent.ToggleDaily(isDaily))

        // Then
        val state = alarmEditViewModel.state.first()
        assertEquals(setOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"), state.selectedDays)
        assertEquals(isDaily, state.isDailyChecked)
    }

    @Test
    fun `toggle daily - disable daily`() = testScope.runTest {
        // Given
        val isDaily = false
        alarmEditViewModel.processIntent(AlarmEditIntent.ToggleDaily(true)) // enable first

        // When
        alarmEditViewModel.processIntent(AlarmEditIntent.ToggleDaily(isDaily)) // disable it

        // Then
        val state = alarmEditViewModel.state.first()
        assertTrue(state.selectedDays.isEmpty())
        assertEquals(isDaily, state.isDailyChecked)
    }

    @Test
    fun `set audio file`() = testScope.runTest {
        // Given
        val uri = mockk<Uri>()
        val audioPath = "path/to/audio.mp3"
        coEvery { selectAudioFileUseCase.execute(uri) } returns audioPath

        // When
        alarmEditViewModel.processIntent(AlarmEditIntent.SetAudioFile(uri))

        // Then
        val state = alarmEditViewModel.state.first()
        assertEquals(audioPath, state.selectedAudioPath)
    }

    @Test
    fun `save alarm - new alarm`() = testScope.runTest {
        // Given
        alarmEditViewModel.processIntent(AlarmEditIntent.SetTime(6, 0))
        alarmEditViewModel.processIntent(AlarmEditIntent.ToggleDay("Mon"))
        val initialState = alarmEditViewModel.state.first()

        coEvery { insertAlarmUseCase(any()) } //just Runs

        // When
        alarmEditViewModel.processIntent(AlarmEditIntent.SaveAlarm)

        // Then
        val state = alarmEditViewModel.state.first()
        coVerify { insertAlarmUseCase(any()) }
        //assertFalse(state.isLoading)

        val alarm = state.alarm
        assertNotNull(alarm)
        assertEquals(6, alarm?.hour)
        assertEquals(0, alarm?.minute)
        assertEquals(listOf(1), alarm?.repeatDays)
    }

    @Test
    fun `save alarm - update existing alarm`() = testScope.runTest {
        // Given
        val existingAlarm = Alarm(
            id = 1L,
            hour = 7,
            minute = 30,
            isEnabled = true,
            repeatDays = listOf(1),
            isRepeating = true,
            label = "Wake Up",
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = true,
            audioPath = "/path/to/audio"
        )
        coEvery { getAlarmByIdUseCase(1L) } returns Result.Success(existingAlarm)
        alarmEditViewModel.processIntent(AlarmEditIntent.LoadAlarm(1L))

        alarmEditViewModel.processIntent(AlarmEditIntent.SetTime(8, 0))
        alarmEditViewModel.processIntent(AlarmEditIntent.ToggleVibration(false))
        val initialState = alarmEditViewModel.state.first()

        coEvery { updateAlarmUseCase(any()) } //just Runs

        // When
        alarmEditViewModel.processIntent(AlarmEditIntent.SaveAlarm)

        // Then
        val state = alarmEditViewModel.state.first()
        coVerify { updateAlarmUseCase(any()) }
        //assertFalse(state.isLoading)

        val alarm = state.alarm
        assertNotNull(alarm)
        assertEquals(1L, alarm?.id)
        assertEquals(7, alarm?.hour)
        assertEquals(30, alarm?.minute)
        assertFalse(alarm!!.isVibrationEnabled)
    }

    @Test
    fun `create new alarm`() = testScope.runTest {
        // When
        alarmEditViewModel.processIntent(AlarmEditIntent.NewAlarm)

        // Then
        val state = alarmEditViewModel.state.first()
        assertNotNull(state.alarm)
        assertEquals(0L, state.alarm?.id)
        //assertEquals(0, state.alarm?.hour)
        //assertEquals(0, state.alarm?.minute)
        assertTrue(state.alarm?.isEnabled ?: false)
        assertTrue(state.alarm?.repeatDays?.isEmpty() ?: false)
        assertFalse(state.alarm?.isRepeating ?: true)
        assertEquals("Wake Up", state.alarm?.label)
        assertFalse(state.alarm?.isVibrationEnabled ?: true)
        assertEquals("", state.alarm?.audioPath)
    }
}




