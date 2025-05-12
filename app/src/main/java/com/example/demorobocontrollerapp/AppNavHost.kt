package com.example.demorobocontrollerapp

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.demorobocontrollerapp.loading.DisplayLoading
import com.example.demorobocontrollerapp.controls.DisplayApp
import com.example.demorobocontrollerapp.settings.DisplaySetting
import com.example.demorobocontrollerapp.voicecontrol.VoiceCommandScreen

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
                 },
                onVoiceCommandPressed = {
                    navController.navigate("voiceControl")
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

        composable("voicecontrol") {
            VoiceCommandScreen(
                viewModel = hiltViewModel(),
                onBackPressed = {
                    navController.popBackStack() // Go back to main screen
                }
            )
        }
    }
}


