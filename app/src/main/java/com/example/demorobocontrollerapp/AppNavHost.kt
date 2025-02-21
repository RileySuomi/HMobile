package com.example.demorobocontrollerapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "loading") {
        composable("loading") {
            DisplayLoading(
                onTimeout = {
                    navController.navigate("main") {
                        popUpTo("loading") { inclusive = true }
                    }
                }
            )
        }

        composable("main") {
            DisplayApp(
                viewModel = RobotControllerViewModel()
            )
        }

        composable("settings") {
            DisplaySetting(
                viewModel = SettingViewModel(),
                onBackPressed = {
                    navController.popBackStack() // Go back to main screen
                }
            )
        }
    }
}
