package com.example.waterbalance.ui.screens

//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.waterbalance.R
import com.example.waterbalance.data.WaterTrackerModel
import com.example.waterbalance.viewmodels.MainViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel,
    navController: NavController
) {
    val waterTracker by viewModel.waterTracker.collectAsState()
    var showResetDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Вода") },
                actions = {
                    IconButton(onClick = { showResetDialog = true }) {
                        Icon(Icons.Default.Refresh, "Сбросить")
                    }
                    IconButton(onClick = { navController.navigate("settings_screen") }) {
                        Icon(Icons.Default.Settings, "Настройки")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_water_screen") },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, "Добавить")
            }
        }
    ) { padding ->
        WaterContent(
            waterTracker = waterTracker,
            onAddWater = { amount -> viewModel.addConsumedWater(amount) },
            onReset = { viewModel.resetConsumedWater() },
            padding = padding,
            showResetDialog = showResetDialog,
            onDismissResetDialog = { showResetDialog = false }
        )
    }
}

@Composable
private fun WaterContent(
    waterTracker: WaterTrackerModel,
    onAddWater: (Int) -> Unit,
    onReset: () -> Unit,
    padding: PaddingValues,
    showResetDialog: Boolean,
    onDismissResetDialog: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressBar(
            progress = waterTracker.getProgressPercentage(),
            consumedWater = waterTracker.consumedWater,
            dailyGoal = waterTracker.dailyGoal
        )

        Spacer(modifier = Modifier.height(24.dp))

        WaterControls(
            glassSize = waterTracker.glassSize,
            consumedWater = waterTracker.consumedWater,
            onAddWater = onAddWater
        )
    }

    if (showResetDialog) {
        ResetDialog(
            onConfirm = {
                onReset()
                onDismissResetDialog()
            },
            onDismiss = onDismissResetDialog
        )
    }
}

@Composable
private fun WaterControls(
    glassSize: Int,
    consumedWater: Int,
    onAddWater: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RoundButton(
            icon = painterResource(id = R.drawable.ic_empty_glass),
            enabled = consumedWater > 0
        ) {
            onAddWater(-glassSize)
        }

        RoundButton(
            icon = painterResource(id = R.drawable.ic_full_glass),
            enabled = true
        ) {
            onAddWater(glassSize)
        }
    }
}

@Composable
private fun ResetDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Подтверждение") },
        text = { Text("Вы уверены, что хотите сбросить прогресс?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Да")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Нет")
            }
        }
    )
}
@Composable
private fun RoundButton(
    icon: Painter, // Изменяем тип на Painter
    enabled: Boolean,
    onClick: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .background(
                color = if (enabled) Color.Blue else Color.Gray,
                shape = CircleShape
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(color = Color.White), // Ripple-эффект
                enabled = enabled,
                onClick = onClick
            )
    ) {
        Icon(
            painter = icon, // Используем painter вместо imageVector
            contentDescription = null,
            tint = if (enabled) Color.White else Color.DarkGray,
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun CircularProgressBar(progress: Float, consumedWater: Int, dailyGoal: Int) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 500), label = "" // Плавная анимация за 500 мс
    )

    Box(contentAlignment = Alignment.Center) {
        CircularProgressIndicator(
            progress = animatedProgress,
            color = Color.Blue,
            strokeWidth = 12.dp,
            modifier = Modifier.size(200.dp)
        )
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$consumedWater",
                color = Color.Black
            )
            Text(
                text = "——",
                color = Color.LightGray
            )
            Text(
                text = "$dailyGoal",
                color = Color.Black
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "мл",
                color = Color.Gray
            )
        }
    }
}