package com.example.waterbalance.ui.screens

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.waterbalance.viewmodels.MainViewModel
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val waterTracker by viewModel.waterTracker.collectAsState()

    // Храним временные значения в rememberSaveable и синхронизируем при изменении модели
    var tempDailyGoal by rememberSaveable { mutableFloatStateOf(waterTracker.dailyGoal.toFloat()) }
    var tempGlassSize by rememberSaveable { mutableFloatStateOf(waterTracker.glassSize.toFloat()) }

    // Обновляем временные значения при изменении waterTracker (один раз)
    LaunchedEffect(waterTracker.dailyGoal, waterTracker.glassSize) {
        tempDailyGoal = waterTracker.dailyGoal.toFloat()
        tempGlassSize = waterTracker.glassSize.toFloat()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Назад")
                    }
                }
            )
        }
    ) { padding ->
        val dailyGoalSteps = (500..5000 step 500).map { it.toFloat() }
        val glassSizeSteps = (50..1000 step 50).map { it.toFloat() }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            SettingItemDiscrete(
                label = "Норма жидкости",
                value = tempDailyGoal,
                steps = dailyGoalSteps,
                onValueChangeFinished = {
                    tempDailyGoal = it
                    viewModel.updateDailyGoal(it.toInt())
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            SettingItemDiscrete(
                label = "Объем стакана",
                value = tempGlassSize,
                steps = glassSizeSteps,
                onValueChangeFinished = {
                    tempGlassSize = it
                    viewModel.updateGlassSize(it.toInt())
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Готово")
            }
        }
    }
}

@Composable
fun SettingItemDiscrete(
    label: String,
    value: Float,
    steps: List<Float>,
    onValueChangeFinished: (Float) -> Unit
) {
    var sliderPosition by remember { mutableIntStateOf(steps.indexOf(value).coerceAtLeast(0)) }

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = "${steps[sliderPosition].toInt()} мл",
            style = MaterialTheme.typography.displaySmall.copy(color = Color.Blue),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Slider(
            value = sliderPosition.toFloat(),
            onValueChange = { sliderPosition = it.roundToInt().coerceIn(steps.indices) },
            onValueChangeFinished = {
                onValueChangeFinished(steps[sliderPosition])
            },
            valueRange = 0f..(steps.size - 1).toFloat(),
            steps = steps.size - 2, // т.к. step = количество промежутков
            colors = SliderDefaults.colors(
                thumbColor = Color.Blue,
                activeTrackColor = Color.Blue.copy(alpha = 0.3f)
            )
        )
    }
}