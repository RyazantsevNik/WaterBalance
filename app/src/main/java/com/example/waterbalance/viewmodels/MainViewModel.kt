package com.example.waterbalance.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.waterbalance.data.WaterRepository
import com.example.waterbalance.data.WaterTrackerModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val repository: WaterRepository) : ViewModel() {
    // CollectAsState теперь из Flow
    val waterTracker: StateFlow<WaterTrackerModel> =
        repository.getWaterTrackerData()
            .stateIn(
                viewModelScope,
                SharingStarted.Eagerly,
                WaterTrackerModel()
            )

    fun updateDailyGoal(newGoal: Int) = viewModelScope.launch {
        repository.updateDailyGoal(newGoal)
    }

    fun updateGlassSize(newSize: Int) = viewModelScope.launch {
        repository.updateGlassSize(newSize)
    }

    fun addConsumedWater(amount: Int) = viewModelScope.launch {
        repository.addConsumedWater(amount)
    }

    fun resetConsumedWater() = viewModelScope.launch {
        repository.resetConsumedWater()
    }
}