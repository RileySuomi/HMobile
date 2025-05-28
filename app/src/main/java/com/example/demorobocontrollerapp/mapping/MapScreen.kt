@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.demorobocontrollerapp

import android.annotation.SuppressLint
import android.graphics.Matrix
import android.util.Log
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demorobocontrollerapp.controls.RobotConnection
import com.example.demorobocontrollerapp.controls.RobotControllerViewModel
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
                    viewModel = hiltViewModel(),
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
    }
}


@SuppressLint("ClickableViewAccessibility")
@Composable
fun MapScreen(
    viewModel: RobotControllerViewModel = hiltViewModel(),
    onBackPressed: () -> Unit
) {
    val connection = RobotConnection();
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Navigation", fontSize = 32.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onBackPressed() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                var imageViewRef: ImageView? = null

                // Embed ImageView using AndroidView
                Log.d("2", "2")
                val mapBitmap by viewModel.mapBitmap.collectAsState()
                Log.d("3", "3")
                AndroidView(
                    factory = { context ->
                        ImageView(context).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                1000
                            )
                            scaleType = ImageView.ScaleType.FIT_CENTER

                            setOnTouchListener { v, event ->
                                if (event.action == MotionEvent.ACTION_DOWN) {
                                    val imageView = v as ImageView
                                    val x = event.x
                                    val y = event.y

                                    // Convert to map pixel coordinates
                                    val imageMatrix = imageView.imageMatrix
                                    val inverse = Matrix()
                                    imageMatrix.invert(inverse)
                                    val touchPoint = floatArrayOf(x, y)
                                    inverse.mapPoints(touchPoint)

                                    val mapX = touchPoint[0].toInt()
                                    val mapY = touchPoint[1].toInt()

                                    // Send the clicked coordinates
                                    viewModel.sendClickedCoordinate(mapX, mapY)
                                }
                                true
                            }
                        }
                    },
                    update = { imageView ->
                        mapBitmap?.let {
                            imageView.setImageBitmap(it)
                        }
                    }
                )



                Spacer(modifier = Modifier.height(16.dp))

                // Add Map button
                Button(
                    onClick = {
                        viewModel.getMap()
                    },
                    modifier = Modifier
                        .width(150.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue, contentColor = Color.White)
                ) {
                    Text("Add Map")
                }
            }
        }
    )
}


fun addMap() {
    val robotConnection = RobotConnection()
    println("Add Map clicked")
}

fun loadMap() {
    // Add your logic for loading a map
    println("Load Map clicked")
}