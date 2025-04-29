//package com.example.data.alarm.dao
//
//
//import android.content.Context
//import androidx.room.Room
//import com.example.data.alarm.dataSource.AlarmDatabase
//import com.example.data.alarm.entity.AlarmEntity
//import io.mockk.every
//import io.mockk.mockk
//import kotlinx.coroutines.flow.first
//import kotlinx.coroutines.runBlocking
//import kotlinx.coroutines.test.runTest
//import org.junit.After
//import org.junit.Assert.assertEquals
//import org.junit.Assert.assertNull
//import org.junit.Before
//import org.junit.Test
//import org.junit.runner.RunWith
//import org.junit.runners.JUnit4
//import java.io.File
//
//@RunWith(JUnit4::class)
//class AlarmDaoTest {
//
//    private lateinit var alarmDao: AlarmDao
//    private lateinit var db: AlarmDatabase
//    private val mockContext = mockk<Context>(relaxed = true)
//
//    @Before
//    fun createDb() {
//        // Mock necessary context methods
//        val filesDir = File("build/tmp/test")
//        filesDir.mkdirs()
//        every { mockContext.getDatabasePath(any()) } returns File(filesDir, "test-db")
//        every { mockContext.filesDir } returns filesDir
//
//        db = Room.inMemoryDatabaseBuilder(
//            mockContext, AlarmDatabase::class.java
//        )
//            .allowMainThreadQueries()
//            .build()
//        alarmDao = db.alarmDao()
//    }
//
////    @Before
////    fun createDb() {
////        val context = ApplicationProvider.getApplicationContext<Context>()
////
////        db = Room.inMemoryDatabaseBuilder(
////            context, AlarmDatabase::class.java
////        )
////            .allowMainThreadQueries()
////            .build()
////        alarmDao = db.alarmDao()
////    }
//
//    @After
//    fun closeDb() {
//        db.close()
//    }
//
//    @Test
//    fun insertAndGetAlarm() = runBlocking {
//        val alarm = AlarmEntity(
//            id = 1,
//            hour = 1,
//            minute = 1,
//            isEnabled = true,
//            repeatDays = "1",
//            isRepeating = true,
//            label = "1",
//            timestamp = 1,
//            isVibrationEnabled = true,
//            audioPath = "1"
//        )
//        alarmDao.insert(alarm)
//        val byId = alarmDao.getAlarmById(1)
//        assertEquals(alarm, byId)
//    }
//
//    @Test
//    fun testInsertAndFetchAlarms() = runTest {
//        val alarm = AlarmEntity(
//            id = 1,
//            hour = 1,
//            minute = 1,
//            isEnabled = true,
//            repeatDays = "1",
//            isRepeating = true,
//            label = "1",
//            timestamp = 1,
//            isVibrationEnabled = true,
//            audioPath = "1"
//        )
//        alarmDao.insert(alarm)
//
//        val alarms = alarmDao.getAllAlarms().first()
//        assertEquals(1, alarms.size)
//        assertEquals(alarm.hour, alarms[0].hour)
//    }
//
//    @Test
//    fun updateAlarm() = runBlocking {
//        val alarm = AlarmEntity(
//            id = 1,
//            hour = 1,
//            minute = 1,
//            isEnabled = true,
//            repeatDays = "1",
//            isRepeating = true,
//            label = "1",
//            timestamp = 1,
//            isVibrationEnabled = true,
//            audioPath = "1"
//        )
//        alarmDao.insert(alarm)
//
//        val updatedAlarm = AlarmEntity(
//            id = 1,
//            hour = 2,
//            minute = 2,
//            isEnabled = false,
//            repeatDays = "2",
//            isRepeating = false,
//            label = "2",
//            timestamp = 2,
//            isVibrationEnabled = false,
//            audioPath = "2"
//        )
//        alarmDao.update(updatedAlarm)
//        val byId = alarmDao.getAlarmById(1)
//        assertEquals(updatedAlarm, byId)
//    }
//
//    @Test
//    fun deleteAlarm() = runBlocking {
//        val alarm = AlarmEntity(
//            id = 1,
//            hour = 1,
//            minute = 1,
//            isEnabled = true,
//            repeatDays = "1",
//            isRepeating = true,
//            label = "1",
//            timestamp = 1,
//            isVibrationEnabled = true,
//            audioPath = "1"
//        )
//        alarmDao.insert(alarm)
//        alarmDao.delete(alarm)
//        val byId = alarmDao.getAlarmById(1)
//        assertNull(byId)
//    }
//
//    @Test
//    fun getAllAlarms() = runBlocking {
//        val alarm1 = AlarmEntity(
//            id = 1,
//            hour = 1,
//            minute = 1,
//            isEnabled = true,
//            repeatDays = "1",
//            isRepeating = true,
//            label = "1",
//            timestamp = 1,
//            isVibrationEnabled = true,
//            audioPath = "1"
//        )
//        val alarm2 = AlarmEntity(
//            id = 2,
//            hour = 2,
//            minute = 2,
//            isEnabled = false,
//            repeatDays = "2",
//            isRepeating = false,
//            label = "2",
//            timestamp = 2,
//            isVibrationEnabled = false,
//            audioPath = "2"
//        )
//        alarmDao.insert(alarm1)
//        alarmDao.insert(alarm2)
//        val allAlarms = alarmDao.getAllAlarms().first()
//        assertEquals(listOf(alarm1, alarm2), allAlarms)
//    }
//
////    @Test
////    fun testInsertAndFetchAlarms() = runTest {
////        val alarm = AlarmEntity(time = 123456789, isActive = true)
////        alarmDao.insertAlarm(alarm)
////
////        val alarms = alarmDao.getAllAlarms()
////        assertEquals(1, alarms.size)
////        assertEquals(alarm.time, alarms[0].time)
////    }
//}
//
//
