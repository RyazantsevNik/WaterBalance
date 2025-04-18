package com.example.waterbalance.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.waterbalance.viewmodels.MainViewModel
import org.koin.androidx.compose.getViewModel

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(
        navController  = navController,
        startDestination = "main_screen",
        route = "root_graph"
    ) {
        composable("main_screen") {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry("root_graph")
            }

            val vm: MainViewModel = getViewModel(viewModelStoreOwner = parentEntry)
            MainScreen(viewModel = vm, navController = navController)
        }

        composable("add_water_screen") {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry("root_graph")
            }
            val vm: MainViewModel = getViewModel(viewModelStoreOwner = parentEntry)
            AddWaterScreen(viewModel = vm, navController = navController)
        }

        composable("settings_screen") {
            val parentEntry = remember(navController) {
                navController.getBackStackEntry("root_graph")
            }
            val vm: MainViewModel = getViewModel(viewModelStoreOwner = parentEntry)
            SettingsScreen(viewModel = vm, navController = navController)
        }
    }
}