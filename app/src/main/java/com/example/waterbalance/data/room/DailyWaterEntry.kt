package com.example.waterbalance.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "daily_water_entry")
data class DailyWaterEntry(
    @PrimaryKey val date: String, // например, "2025-04-18"
    val consumedWater: Int,
    val dailyGoal: Int,
    val glassSize: Int
)
//@Entity(tableName = "daily_water")
//data class DailyWaterEntry(
//    @PrimaryKey val date: String,      // "2025-04-18"
//    val consumedWater: Int,
//    val dailyGoal: Int,
//    val glassSize: Int
//)