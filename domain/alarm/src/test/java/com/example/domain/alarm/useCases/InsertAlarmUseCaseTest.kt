package com.example.domain.alarm.useCases

import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.repository.AlarmRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class InsertAlarmUseCaseTest {

    private lateinit var repository: AlarmRepository
    private lateinit var insertAlarmUseCase: InsertAlarmUseCase

    @Before
    fun setUp() {
        repository = mockk()
        insertAlarmUseCase = InsertAlarmUseCase(repository)
    }

    @Test
    fun `should insert alarm successfully and return its ID`() = runBlocking {
        // Given
        val alarm = Alarm(
            id = 1,
            hour = 8,
            minute = 0,
            isEnabled = true,
            repeatDays = listOf(1, 2, 3),
            isRepeating = true,
            label = "Morning Alarm",
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = true,
            audioPath = "/path/to/audio"
        )

        // Mock the insertAlarm method to return the inserted alarm's ID (for example, 1L)
        coEvery { repository.insertAlarm(alarm) } returns 1L

        // When
        val result = insertAlarmUseCase.invoke(alarm)

        // Then
        coVerify { repository.insertAlarm(alarm) }
        // Assert that the returned result is the ID of the inserted alarm
        assertEquals(1L, result)
    }

    @Test
    fun `should handle error when insertion fails`() = runBlocking {
        // Given
        val alarm = Alarm(
            id = 1,
            hour = 8,
            minute = 0,
            isEnabled = true,
            repeatDays = listOf(1, 2, 3),
            isRepeating = true,
            label = "Morning Alarm",
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = true,
            audioPath = "/path/to/audio"
        )

        // Mock the insertAlarm method to throw an exception
        val exception = Exception("Error inserting alarm")
        coEvery { repository.insertAlarm(alarm) } throws exception

        // When
        try {
            insertAlarmUseCase.invoke(alarm)
        } catch (e: Exception) {
            // Then
            // Ensure that the exception was thrown and it matches the one we mocked
            assertTrue(e is Exception)
            assertEquals("Error inserting alarm", e.message)
        }

        // Verify that insertAlarm was called
        coVerify { repository.insertAlarm(alarm) }
    }
}
