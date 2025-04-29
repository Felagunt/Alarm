package com.example.alarmmess.di

import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.alarm.dao.AlarmDao
import com.example.data.alarm.dataSource.AlarmDatabase
import org.koin.dsl.module

val databaseModule = module {

        single {
            Room.databaseBuilder(
                get(),
                AlarmDatabase::class.java,
                "alarm_database"
            )
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        Log.d("RoomDatabase", "Database created")
                    }
                })
                //.fallbackToDestructiveMigration()
                .build()
        }

        //single { get<AlarmDatabase>().alarmDao() }
        single<AlarmDao> {
            val database = get<AlarmDatabase>()
            database.alarmDao()
        }
}
