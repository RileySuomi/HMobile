package com.example.demorobocontrollerapp

import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.text.format.Formatter.formatIpAddress
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.demorobocontrollerapp.ui.theme.DemoRoboControllerAppTheme

//class SettingActivity:ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContent {
//            val viewModel: SettingViewModel by viewModels() // initialize viewModel object
//            DemoRoboControllerAppTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    DisplaySetting(
//                        viewModel,
//                        onBackPressed = {
//                            val intent = Intent(this@SettingActivity, MainActivity::class.java)
//                            startActivity(intent)
//                            finish()
//                        }
//                    )
//                }
//            }
//        }
//    }
//}