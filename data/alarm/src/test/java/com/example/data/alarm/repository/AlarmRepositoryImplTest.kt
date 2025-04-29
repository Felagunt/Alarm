package com.example.data.alarm.repository
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.data.alarm.dao.AlarmDao
import com.example.data.alarm.entity.AlarmEntity
import com.example.data.alarm.mappers.toEntity
import com.example.domain.alarm.model.Alarm
import com.google.gson.Gson
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlarmRepositoryImplTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // For testing LiveData and Flow

    private val alarmDao: AlarmDao = mockk(relaxed = true)
    private val repository = AlarmRepositoryImpl(alarmDao)

    @Test
    fun `insertAlarm should call insert on alarmDao and return inserted ID`() = runTest {
        // Arrange
        val alarm = Alarm(id = 1, hour = 7, minute = 30, isEnabled = true, repeatDays = listOf(1, 2, 3), isRepeating = false, label = "Morning Alarm", timestamp = 1234567890, isVibrationEnabled = true, audioPath = "")
        val entity = alarm.toEntity()
        coEvery { alarmDao.insert(entity) } returns 1L // Returning a mock ID for insertion

        // Act
        val result = repository.insertAlarm(alarm)

        // Assert
        coVerify { alarmDao.insert(entity) }
        assertEquals(1L, result) // Assert that the returned ID is as expected
    }

    @Test
    fun `updateAlarm should call update on alarmDao`() = runTest {
        // Arrange
        val alarm = Alarm(id = 1, hour = 8, minute = 15, isEnabled = false, repeatDays = listOf(0, 4), isRepeating = true, label = "Evening Alarm", timestamp = 1234567890, isVibrationEnabled = false, audioPath = "")
        val entity = alarm.toEntity()
        coEvery { alarmDao.update(entity) } just Runs

        // Act
        repository.updateAlarm(alarm)

        // Assert
        coVerify { alarmDao.update(entity) }
    }

    @Test
    fun `deleteAlarm should call delete on alarmDao`() = runTest {
        // Arrange
        val alarm = Alarm(id = 1, hour = 6, minute = 45, isEnabled = true, repeatDays = listOf(5, 6), isRepeating = false, label = "Weekend Alarm", timestamp = 1234567890, isVibrationEnabled = true, audioPath = "")
        val entity = alarm.toEntity()
        coEvery { alarmDao.delete(entity) } just Runs

        // Act
        repository.deleteAlarm(alarm)

        // Assert
        coVerify { alarmDao.delete(entity) }
    }

    @Test
    fun `getAllAlarms should return transformed list from alarmDao`() = runTest {
        // Arrange
        val entities = listOf(
            AlarmEntity(
                id = 1,
                hour = 7,
                minute = 30,
                isEnabled = true,
                repeatDays = "1,2,3",
                isRepeating = false,
                label = "Morning Alarm",
                timestamp = 1234567890,
                isVibrationEnabled = true,
                audioPath = ""
            ),
            AlarmEntity(
                id = 2,
                hour = 9,
                minute = 0,
                isEnabled = false,
                repeatDays = "4,5",
                isRepeating = true,
                label = "Workout Alarm",
                timestamp = 1234567890,
                isVibrationEnabled = false,
                audioPath = ""
            )
        )
        coEvery { alarmDao.getAllAlarms() } returns flowOf(entities)

        // Act
        val result = repository.getAllAlarms()

        // Assert
        result.collect { alarms ->
            assertEquals(2, alarms.size)
            assertEquals(listOf(1, 2, 3), alarms[0].repeatDays)
            assertEquals(listOf(4, 5), alarms[1].repeatDays)
        }
    }

    @Test
    fun `getAlarmById should return transformed entity from alarmDao`() = runTest {
        // Arrange
        val entity = AlarmEntity(
            id = 1,
            hour = 7,
            minute = 30,
            isEnabled = true,
            repeatDays = "1,2,3",
            isRepeating = false,
            label = "Morning Alarm",
            timestamp = 1234567890,
            isVibrationEnabled = true,
            audioPath = ""
        )
        coEvery { alarmDao.getAlarmById(1) } returns entity

        // Act
        val result = repository.getAlarmById(1)

        // Assert
        assertEquals(7, result?.hour)
        assertEquals(listOf(1, 2, 3), result?.repeatDays)
    }
}
