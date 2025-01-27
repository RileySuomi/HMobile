package com.example.demorobocontrollerapp

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "loading") {
        // Loading Screen
        composable("loading") {
            DisplayLoading(
                onTimeout = {
                    navController.navigate("main") {
                        popUpTo("loading") { inclusive = true } // Remove loading from the back stack
                    }
                }
            )
        }

        // Main Screen
        composable("main") {
            DisplayApp(
                viewModel = RobotControllerViewModel(),
                onSettingPressed = {
                    navController.navigate("settings")
                }
            )
        }

        // Setting Screen
        composable("settings") {
            DisplaySetting(
                viewModel = SettingViewModel(),
                onBackPressed = {
                    navController.popBackStack() // Navigate back to the main screen
                }
            )
        }
    }
}