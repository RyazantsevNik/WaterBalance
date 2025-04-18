package com.example.waterbalance.data

import com.example.waterbalance.data.room.DailyWaterEntry
import com.example.waterbalance.data.room.WaterDao
import kotlinx.coroutines.flow.*
import java.time.LocalDate

class WaterRepository(private val dao: WaterDao) {

    private fun todayKey(): String = LocalDate.now().toString()

    fun getWaterTrackerData(): Flow<WaterTrackerModel> =
        dao.getEntryFlow(todayKey())
            .map { entry ->
                entry?.let {
                    WaterTrackerModel(
                        dailyGoal = it.dailyGoal,
                        consumedWater = it.consumedWater,
                        glassSize = it.glassSize
                    )
                } ?: WaterTrackerModel()
            }

    suspend fun updateDailyGoal(newGoal: Int) {
        val today = todayKey()
        val current = dao.getEntry(today)
        val entry = DailyWaterEntry(
            date = today,
            consumedWater = current?.consumedWater ?: 0,
            dailyGoal = newGoal,
            glassSize = current?.glassSize ?: WaterTrackerModel().glassSize
        )
        dao.upsert(entry)
    }

    suspend fun updateGlassSize(newSize: Int) {
        val today = todayKey()
        val current = dao.getEntry(today)
        val entry = DailyWaterEntry(
            date = today,
            consumedWater = current?.consumedWater ?: 0,
            dailyGoal = current?.dailyGoal ?: WaterTrackerModel().dailyGoal,
            glassSize = newSize
        )
        dao.upsert(entry)
    }

    suspend fun addConsumedWater(amount: Int) {
        val today = todayKey()
        val current = dao.getEntry(today)
        val entry = DailyWaterEntry(
            date = today,
            consumedWater = (current?.consumedWater ?: 0) + amount,
            dailyGoal = current?.dailyGoal ?: WaterTrackerModel().dailyGoal,
            glassSize = current?.glassSize ?: WaterTrackerModel().glassSize
        )
        dao.upsert(entry)
    }

    suspend fun resetConsumedWater() {
        val today = todayKey()
        val current = dao.getEntry(today)
        val entry = DailyWaterEntry(
            date = today,
            consumedWater = 0,
            dailyGoal = current?.dailyGoal ?: WaterTrackerModel().dailyGoal,
            glassSize = current?.glassSize ?: WaterTrackerModel().glassSize
        )
        dao.upsert(entry)
    }

    fun getAllHistory(): Flow<List<DailyWaterEntry>> = dao.getAllEntries()
}
