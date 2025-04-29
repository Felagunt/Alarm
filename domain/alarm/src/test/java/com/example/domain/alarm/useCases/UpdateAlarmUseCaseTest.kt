package com.example.domain.alarm.useCases

import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.repository.AlarmRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class UpdateAlarmUseCaseTest {

    private lateinit var repository: AlarmRepository
    private lateinit var updateAlarmUseCase: UpdateAlarmUseCase

    @Before
    fun setUp() {
        repository = mockk()
        updateAlarmUseCase = UpdateAlarmUseCase(repository)
    }

    @Test
    fun `should return success result when alarm is updated successfully`() = runBlocking {
        // Given
        val alarm =  Alarm(
            id = 1,
            hour = 8,
            minute = 0,
            isEnabled = true,
            repeatDays = listOf(1, 2, 3),
            isRepeating = true,
            label = "Updated Alarm",
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = true,
            audioPath = "/path/to/audio"
        )
        coEvery { repository.updateAlarm(alarm) } returns Unit

        // When
        val result = updateAlarmUseCase.invoke(alarm)

        // Then
        assertTrue(result is com.example.core.comon.utils.Result.Success)
        coVerify { repository.updateAlarm(alarm) }
    }

    @Test
    fun `should return error result when an exception occurs while updating alarm`() = runBlocking {
        // Given
        val alarm = Alarm(
            id = 1,
            hour = 8,
            minute = 0,
            isEnabled = true,
            repeatDays = listOf(1, 2, 3),
            isRepeating = true,
            label = "Updated Alarm",
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = true,
            audioPath = "/path/to/audio"
        )
        val exception = Exception("Error updating alarm")
        coEvery { repository.updateAlarm(alarm) } throws exception

        // When
        val result = updateAlarmUseCase.invoke(alarm)

        // Then
        assertTrue(result is com.example.core.comon.utils.Result.Error)
        val errorResult = result as com.example.core.comon.utils.Result.Error
        assertEquals(exception, errorResult.exception)
        coVerify { repository.updateAlarm(alarm) }
    }
}