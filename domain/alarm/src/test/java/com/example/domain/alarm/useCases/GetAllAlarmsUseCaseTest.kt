package com.example.domain.alarm.useCases

import com.example.domain.alarm.model.Alarm
import com.example.domain.alarm.repository.AlarmRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class GetAllAlarmsUseCaseTest {

    private lateinit var repository: AlarmRepository
    private lateinit var getAllAlarmsUseCase: GetAllAlarmsUseCase

    @Before
    fun setUp() {
        repository = mockk()
        getAllAlarmsUseCase = GetAllAlarmsUseCase(repository)
    }

    @Test
    fun `should return success result when alarms are fetched successfully`() = runBlocking {
        // Given
        val alarmList = listOf(
            Alarm(
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
        )
        coEvery { repository.getAllAlarms() } returns flowOf(alarmList)

        // When
        val result = getAllAlarmsUseCase.invoke().toList()

        // Then
        assertTrue(result.first() is com.example.core.comon.utils.Result.Success)
        val successResult = result.first() as com.example.core.comon.utils.Result.Success
        assertEquals(alarmList, successResult.data)
        coVerify { repository.getAllAlarms() }
    }

    @Test
    fun `should return error result when an exception occurs`() = runBlocking {
        // Given
        val exception = Exception("Error fetching alarms")
        coEvery { repository.getAllAlarms() } throws exception

        // When
        val result = getAllAlarmsUseCase.invoke().toList()

        // Then
        assertTrue(result.first() is com.example.core.comon.utils.Result.Error)
        val errorResult = result.first() as com.example.core.comon.utils.Result.Error
        assertEquals(exception, errorResult.exception)
        coVerify { repository.getAllAlarms() }
    }
}