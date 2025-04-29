package com.example.data.alarm.dataSource

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.alarm.dao.AlarmDao
import com.example.data.alarm.entity.AlarmEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class AlarmDatabaseTest {

    private lateinit var database: AlarmDatabase
    private lateinit var alarmDao: AlarmDao

    // Setup in-memory database before each test
    @Before
    fun setUp() {
        // Use in-memory database to avoid writing to disk
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AlarmDatabase::class.java
        ).allowMainThreadQueries() // Allow queries to run on the main thread for testing
            .build()

        alarmDao = database.alarmDao()
    }

    // Tear down the database after each test
    @After
    fun tearDown() {
        database.close()
    }

    // Test inserting a new alarm into the database
    @Test
    fun insertShouldInsertAlarmIntoTheDatabase() = runBlocking {
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

        // When
        val insertedId = alarmDao.insert(alarmEntity) // Insert the alarm into the database

        // Then
        val fetchedAlarm = alarmDao.getAlarmById(insertedId) // Fetch the alarm by its ID
        assertEquals(alarmEntity.copy(id = insertedId), fetchedAlarm) // Ensure the fetched alarm matches
    }

    // Test updating an existing alarm
    @Test
    fun updateShouldUpdateAlarmInTheDatabase() = runBlocking {
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
        val insertedId = alarmDao.insert(alarmEntity) // Insert the alarm

        // When updating the alarm
        val updatedAlarm = alarmEntity.copy(id = insertedId, label = "Updated Alarm", hour = 8)
        alarmDao.update(updatedAlarm) // Update the alarm in the database

        // Then
        val fetchedAlarm = alarmDao.getAlarmById(insertedId) // Fetch the updated alarm by ID
        assertEquals(updatedAlarm, fetchedAlarm) // Ensure the updated alarm is fetched
    }

    // Test deleting an alarm from the database
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
        val insertedId = alarmDao.insert(alarmEntity) // Insert the alarm into the database

        // When deleting the alarm
        alarmDao.delete(alarmEntity) // Delete the alarm from the database

        // Ensure that the delete operation is complete before fetching
        val fetchedAlarm = alarmDao.getAlarmById(insertedId) // Try fetching the deleted alarm

        // Then
        assertEquals(null, fetchedAlarm) // Ensure the alarm was deleted
    }


    // Test retrieving all alarms
    @Test
    fun getAllAlarmsShouldReturnAllAlarmsFromTheDatabase() = runBlocking {
        // Given
        val alarmEntity1 = AlarmEntity(
            id = 0, // Auto-generated ID will be assigned
            hour = 7,
            minute = 30,
            isEnabled = true,
            repeatDays = "1,2,3",
            isRepeating = false,
            label = "Test Alarm 1",
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = true,
            audioPath = "path/to/audio"
        )
        val alarmEntity2 = AlarmEntity(
            id = 0, // Auto-generated ID will be assigned
            hour = 8,
            minute = 0,
            isEnabled = true,
            repeatDays = "4,5",
            isRepeating = true,
            label = "Test Alarm 2",
            timestamp = System.currentTimeMillis(),
            isVibrationEnabled = false,
            audioPath = "path/to/audio"
        )

        alarmDao.insert(alarmEntity1) // Insert first alarm
        alarmDao.insert(alarmEntity2) // Insert second alarm

        // When retrieving all alarms
        val allAlarms = alarmDao.getAllAlarms().first() // Collect the first emitted value from the Flow

        // Then
        assertEquals(2, allAlarms.size) // Ensure there are two alarms in the database
        assertEquals("Test Alarm 1", allAlarms[0].label) // Verify the label of the first alarm
        assertEquals("Test Alarm 2", allAlarms[1].label) // Verify the label of the second alarm
    }
}
