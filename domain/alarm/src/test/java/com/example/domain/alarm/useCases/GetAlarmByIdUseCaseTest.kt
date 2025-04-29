package com.example.domain.alarm.useCases

import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.repository.AlarmRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAlarmByIdUseCaseTest {

    private lateinit var repository: AlarmRepository
    private lateinit var getAlarmByIdUseCase: GetAlarmByIdUseCase

    @Before
    fun setUp() {
        // Create a mock of the AlarmRepository
        repository = mockk()
        // Initialize the use case with the mocked repository
        getAlarmByIdUseCase = GetAlarmByIdUseCase(repository)
    }

    @Test
    fun `should return alarm when it exists`() = runBlocking {
        // Given
        val alarmId = 1L
        val alarm = Alarm(id = alarmId, hour = 7, minute = 30, isEnabled = true, repeatDays = listOf(1, 2, 3), isRepeating = false, label = "Morning Alarm", timestamp = 1234567890, isVibrationEnabled = true, audioPath = "")

        // Mock the repository getAlarmById function to return the alarm when called with the given ID
        coEvery { repository.getAlarmById(alarmId) } returns alarm

        // When
        val result = getAlarmByIdUseCase.invoke(alarmId)

        // Then
        // Verify that getAlarmById was called with the correct ID
        coVerify { repository.getAlarmById(alarmId) }

        // Assert that the result is a Success and contains the expected alarm
        assertTrue(result is com.example.core.comon.utils.Result.Success)
        val successResult = result as com.example.core.comon.utils.Result.Success
        assertEquals(alarm, successResult.data)
    }

    @Test
    fun `should return error when alarm does not exist`() = runBlocking {
        // Given
        val alarmId = 1L

        // Mock the repository getAlarmById function to return null (i.e., alarm not found)
        coEvery { repository.getAlarmById(alarmId) } returns null

        // When
        val result = getAlarmByIdUseCase.invoke(alarmId)

        // Then
        // Verify that getAlarmById was called with the correct ID
        coVerify { repository.getAlarmById(alarmId) }

        // Assert that the result is a Success but with null data, as no alarm was found
        assertTrue(result is com.example.core.comon.utils.Result.Success)
        val successResult = result as com.example.core.comon.utils.Result.Success
        assertNull(successResult.data)
    }

    @Test
    fun `should return error when an exception occurs while fetching alarm`() = runBlocking {
        // Given
        val alarmId = 1L
        val exception = Exception("Error fetching alarm")

        // Mock the repository getAlarmById function to throw an exception
        coEvery { repository.getAlarmById(alarmId) } throws exception

        // When
        val result = getAlarmByIdUseCase.invoke(alarmId)

        // Then
        // Verify that getAlarmById was called with the correct ID
        coVerify { repository.getAlarmById(alarmId) }

        // Assert that the result is an Error with the correct exception
        assertTrue(result is com.example.core.comon.utils.Result.Error)
        val errorResult = result as com.example.core.comon.utils.Result.Error
        assertEquals(exception, errorResult.exception)
    }
}