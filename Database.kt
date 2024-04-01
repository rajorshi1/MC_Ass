package com.example.assignment_q2

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}