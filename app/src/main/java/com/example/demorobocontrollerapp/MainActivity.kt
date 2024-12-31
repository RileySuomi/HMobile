package com.example.demorobocontrollerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.demorobocontrollerapp.ui.theme.DemoRoboControllerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel: RobotControllerViewModel by viewModels() // initialize viewModel object
            DemoRoboControllerAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    DisplayApp(viewModel)
                }
            }
        }
    }
}

// TODO: Regularly update to Github 'main' branch using Terminal(local):
//      git add .
//      git commit -m "commit message here"
//      git push origin main