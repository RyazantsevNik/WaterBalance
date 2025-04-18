package com.example.waterbalance.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.waterbalance.viewmodels.MainViewModel
import org.koin.androidx.compose.getViewModel


@Composable
fun AddWaterScreen(viewModel: MainViewModel = getViewModel(), navController: NavController) {
    var amount by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Количество воды (мл)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val waterAmount = amount.toIntOrNull() ?: 0
            if (waterAmount > 0) {
                viewModel.addConsumedWater(waterAmount)
                navController.popBackStack()
            }
        }) {
            Text("Добавить")
        }
    }
}