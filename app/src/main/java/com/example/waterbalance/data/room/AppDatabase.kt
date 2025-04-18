package com.example.waterbalance.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DailyWaterEntry::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun waterDao(): WaterDao
}