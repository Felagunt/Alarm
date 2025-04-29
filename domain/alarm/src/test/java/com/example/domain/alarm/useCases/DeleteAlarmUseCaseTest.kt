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

class DeleteAlarmUseCaseTest {

    private lateinit var repository: AlarmRepository
    private lateinit var deleteAlarmUseCase: DeleteAlarmUseCase

    @Before
    fun setUp() {
        // Create a mock of the AlarmRepository
        repository = mockk()
        // Initialize the use case with the mocked repository
        deleteAlarmUseCase = DeleteAlarmUseCase(repository)
    }

    @Test
    fun `should return success when alarm is deleted successfully`() = runBlocking {
        // Given
        val alarm = Alarm(id = 1, hour = 7, minute = 30, isEnabled = true, repeatDays = listOf(1, 2, 3), isRepeating = false, label = "Morning Alarm", timestamp = 1234567890, isVibrationEnabled = true, audioPath = "")

        // Mock the repository delete function to do nothing (successful deletion)
        coEvery { repository.deleteAlarm(alarm) } returns Unit

        // When
        val result = deleteAlarmUseCase.invoke(alarm)

        // Then
        // Verify that deleteAlarm was called once with the correct alarm object
        coVerify { repository.deleteAlarm(alarm) }

        // Assert that the result is a Success
        assertTrue(result is com.example.core.comon.utils.Result.Success)
    }

    @Test
    fun `should return error when an exception occurs while deleting alarm`() = runBlocking {
        // Given
        val alarm = Alarm(id = 1, hour = 7, minute = 30, isEnabled = true, repeatDays = listOf(1, 2, 3), isRepeating = false, label = "Morning Alarm", timestamp = 1234567890, isVibrationEnabled = true, audioPath = "")

        // Mock the repository delete function to throw an exception
        val exception = Exception("Error deleting alarm")
        coEvery { repository.deleteAlarm(alarm) } throws exception

        // When
        val result = deleteAlarmUseCase.invoke(alarm)

        // Then
        // Verify that deleteAlarm was called once
        coVerify { repository.deleteAlarm(alarm) }

        // Assert that the result is an Error with the correct exception
        assertTrue(result is com.example.core.comon.utils.Result.Error)
        val errorResult = result as com.example.core.comon.utils.Result.Error
        assertEquals(exception, errorResult.exception)
    }
}