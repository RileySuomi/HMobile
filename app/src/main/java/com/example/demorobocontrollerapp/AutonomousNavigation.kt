@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.demorobocontrollerapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import com.example.demorobocontrollerapp.ui.theme.DemoRoboControllerAppTheme

@Preview
@Composable
fun MapPreview() {
    DemoRoboControllerAppTheme {
        // Create a NavController for navigation within Compose
        val navController = rememberNavController()

        // Set up the navigation graph
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                // Display Home screen with a setting button
                MapScreen(
                    viewModel = NavigationModel(),
                    onBackPressed = {
                        // This simulates navigating back to the main screen
                        navController.navigate("main")
                    }
                )
            }
            composable("map") {
                MainActivity()
            }
        }
//       DisplaySetting(viewModel = SettingViewModel(), onBackPressed = {})
    }
}


@Composable
fun MapScreen(viewModel: NavigationModel, onBackPressed: () -> Unit) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Navigation", fontSize = 32.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black
                ),
                navigationIcon = {
                    IconButton(onClick = { onBackPressed()}) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
                //scrollBehavior = scrollBehavior
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(0.dp, 10.dp, 0.dp, 10.dp),
            ) {

            }
        }
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth() // Makes row span full width
                .padding(horizontal = 16.dp), // Adds some spacing on the sides
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(
                onClick = { addMap() },
                modifier = Modifier
                    .width(150.dp)  // Set width
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue, // Background color
                    contentColor = Color.White   // Text color
                )
            ) {
                Text(text = "Add Map")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { loadMap() },
                modifier = Modifier
                    .width(150.dp)  // Set width
                    .height(40.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue, // Background color
                    contentColor = Color.White   // Text color
                )
            ) {
                Text(text = "Load Map")
            }
        }
    }
}

fun addMap() {
    // Add your logic for adding a map
    println("Add Map clicked")
}

fun loadMap() {
    // Add your logic for loading a map
    println("Load Map clicked")
}
