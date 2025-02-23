package com.example.demorobocontrollerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.demorobocontrollerapp.ui.theme.DemoRoboControllerAppTheme

class AppNavHost : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DemoRoboControllerAppTheme {
                NavigationGraph()
            }
        }
    }
}

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
                viewModel = RobotControllerViewModel(),
                // Uncomment when needed
                 onSettingPressed = {
                     navController.navigate("settings")
                 }
            )
        }

        // Settings Screen
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


