package com.example.demorobocontrollerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.demorobocontrollerapp.ui.theme.DemoRoboControllerAppTheme

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "loading") {
        // Loading Screen
        composable("loading") {
            DisplayLoading(
                onTimeout = {
                    navController.navigate("main") {
                        popUpTo("loading") { inclusive = true } // Remove loading from back stack
                    }
                }
            )
        }

        // Main Screen
        composable("main") {
            DisplayApp(
                viewModel = hiltViewModel(),
                // Uncomment when needed
                 onSettingPressed = {
                     navController.navigate("settings")
                 }
            )
        }

        // Settings Screen
        composable("settings") {
            DisplaySetting(
                viewModel = hiltViewModel(),
                onBackPressed = {
                    navController.popBackStack() // Go back to main screen
                }
            )
        }
    }
}


