package com.example.waterbalance.data

data class WaterTrackerModel(
    var dailyGoal: Int = 2000,
    var consumedWater: Int = 0,
    val glassSize: Int = 250
){
    fun getProgressPercentage(): Float{
        return if(dailyGoal == 0)0f else consumedWater.toFloat()/dailyGoal
    }
}

