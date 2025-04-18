package com.example.waterbalance.data.room

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface WaterDao {
    @Query("SELECT * FROM daily_water_entry WHERE date = :date LIMIT 1")
    fun getEntryFlow(date: String): Flow<DailyWaterEntry?>

    @Query("SELECT * FROM daily_water_entry WHERE date = :date LIMIT 1")
    suspend fun getEntry(date: String): DailyWaterEntry?

    @Query("SELECT * FROM daily_water_entry ORDER BY date DESC")
    fun getAllEntries(): Flow<List<DailyWaterEntry>> // для просмотра истории

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entry: DailyWaterEntry)
}
