package com.example.data.alarm.dao

import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.alarm.dataSource.AlarmDatabase
import com.example.data.alarm.entity.AlarmEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class AlarmDaoTest {

    private lateinit var database: AlarmDatabase
    private lateinit var alarmDao: AlarmDao

    @Before
    fun setUp() {
        // Create an in-memory Room database for testing
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AlarmDatabase::class.java
        ).allowMainThreadQueries() // Allow queries to run on the main thread for testing purposes
            .build()

        alarmDao = database.alarmDao()
    }

    @After
    fun tearDown() {
        // Close the database after each test
        database.close()
    }

    @Test
    fun insertShouldInsertAlarmIntoTheDatabase() = runBlocking {
        // Given
        val alarmEntity = AlarmEntity(
            id = 1,
            hour = 7,
            minute = 30,
            isEnabled = true,
            repeatDays = "1,2,3",
            isRepeating = false,
            label = "Test Alarm",
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = true,
            audioPath = "path/to/audio"
        )

        // When
        val insertedId = alarmDao.insert(alarmEntity)

        // Then
        val fetchedAlarm = alarmDao.getAlarmById(insertedId)
        assertEquals(alarmEntity.copy(id = insertedId), fetchedAlarm)
    }

    @Test
    fun updateShouldUpdateAlarmInTheDatabase() = runBlocking {
        // Given
        val alarmEntity = AlarmEntity(
            id = 1,
            hour = 7,
            minute = 30,
            isEnabled = true,
            repeatDays = "1,2,3",
            isRepeating = false,
            label = "Test Alarm",
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = true,
            audioPath = "path/to/audio"
        )
        val insertedId = alarmDao.insert(alarmEntity)

        // Update the alarm details
        val updatedAlarm = alarmEntity.copy(id = insertedId, label = "Updated Alarm", hour = 8)

        // When
        alarmDao.update(updatedAlarm)

        // Then
        val fetchedAlarm = alarmDao.getAlarmById(insertedId)
        assertEquals(updatedAlarm, fetchedAlarm)
    }

    @Test
    fun deleteShouldRemoveAlarmFromTheDatabase() = runBlocking {
        // Given
        val alarmEntity = AlarmEntity(
            id = 0, // Auto-generated ID will be assigned
            hour = 7,
            minute = 30,
            isEnabled = true,
            repeatDays = "1,2,3",
            isRepeating = false,
            label = "Test Alarm",
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = true,
            audioPath = "path/to/audio"
        )

        // Insert the alarm into the database and get the inserted ID
        val insertedId = alarmDao.insert(alarmEntity)

        // Fetch the inserted alarm by the assigned ID
        val insertedAlarm = alarmDao.getAlarmById(insertedId)

        // Log the inserted alarm details
        Log.d("AlarmDatabaseTest", "Inserted alarm: $insertedAlarm")

        // When deleting the alarm by its correct ID
        alarmDao.delete(insertedAlarm!!) // Delete the alarm using the correct object

        // Ensure that the delete operation is complete before fetching again
        val fetchedAlarm = alarmDao.getAlarmById(insertedId) // Try fetching the deleted alarm

        // Log the result of fetching
        Log.d("AlarmDatabaseTest", "Fetched alarm after deletion: $fetchedAlarm")

        // Then
        assertNull(fetchedAlarm) // Ensure the alarm was deleted
    }



    @Test
    fun getAllAlarmsShouldReturnAllAlarmsFromTheDatabase() = runBlocking {
        // Given
        val alarmEntity1 = AlarmEntity(
            id = 1,
            hour = 7,
            minute = 30,
            isEnabled = true,
            repeatDays = "1,2,3",
            isRepeating = false,
            label = "Test Alarm 1",  // Ensure 'label' is present in AlarmEntity
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = true,
            audioPath = "path/to/audio"
        )
        val alarmEntity2 = AlarmEntity(
            id = 2,
            hour = 8,
            minute = 0,
            isEnabled = true,
            repeatDays = "4,5",
            isRepeating = true,
            label = "Test Alarm 2",  // Ensure 'label' is present in AlarmEntity
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = false,
            audioPath = "path/to/audio"
        )

        alarmDao.insert(alarmEntity1)
        alarmDao.insert(alarmEntity2)

        // When
        val allAlarms = alarmDao.getAllAlarms().first()

        // Then
        assertEquals(2, allAlarms.size)
        assertEquals("Test Alarm 1", allAlarms[0].label)
        assertEquals("Test Alarm 2", allAlarms[1].label)
    }
}