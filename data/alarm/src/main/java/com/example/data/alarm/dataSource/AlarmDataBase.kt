package com.example.data.alarm.dataSource

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.alarm.dao.AlarmDao
import com.example.data.alarm.entity.AlarmEntity

@Database(entities = [AlarmEntity::class], version = 2, exportSchema = false)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

//    companion object {
//        @Volatile
//        private var INSTANCE: AlarmDatabase? = null
//
//        fun getDatabase(context: Context): AlarmDatabase {
//            Log.d("RoomDatabase", "Initializing database")
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    AlarmDatabase::class.java,
//                    "alarm_database"
//                ).addCallback(object : RoomDatabase.Callback() {
//                    override fun onCreate(db: SupportSQLiteDatabase) {
//                        super.onCreate(db)
//                        Log.d("RoomDatabase", "Database created")
//                    }
//
//                    override fun onOpen(db: SupportSQLiteDatabase) {
//                        super.onOpen(db)
//                        Log.d("RoomDatabase", "Database opened")
//                    }
//                })
//                    //.fallbackToDestructiveMigration(true)
//                    .build()
//                INSTANCE = instance
//                Log.d("RoomDatabase", "Database initialized")
//                Log.d("RoomDatabase", "Database path: ${context.getDatabasePath("alarm_database").absolutePath}")
//                instance
//            }
//        }
//    }
}
