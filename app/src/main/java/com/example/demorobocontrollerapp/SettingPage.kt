@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.demorobocontrollerapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.demorobocontrollerapp.ui.theme.DemoRoboControllerAppTheme


//use as 'preview'
@Preview(showBackground = true)
@Composable
fun SettingPreview() {
    DemoRoboControllerAppTheme {
        // Create a NavController for navigation within Compose
        val navController = rememberNavController()

        // Set up the navigation graph
        NavHost(navController = navController, startDestination = "home") {
            composable("home") {
                // Display Home screen with a setting button
                DisplaySetting(
                    viewModel = SettingViewModel(),
                    onBackPressed = {
                        // This simulates navigating back to the main screen
                        navController.navigate("main")
                    }
                )
            }
            composable("setting") {
                MainActivity()
            }
        }
//       DisplaySetting(viewModel = SettingViewModel(), onBackPressed = {})
    }
}

@Composable
fun DisplaySetting(viewModel: SettingViewModel, onBackPressed: () -> Unit) {
    //val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    Scaffold(
        //modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),

        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Setting", fontSize = 22.sp) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
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
                    .padding(0.dp, 10.dp, 0.dp, 0.dp)
            ) {
                PhoneIP("001")
                WirelessConnection("wifi_name")
                Port()
            }
        }
    )
}

@Composable
fun PhoneIP(ip: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(10.dp,0.dp),
            text = "Phone IP:",
            color = Color.Black,
        )
        Text(
            modifier = Modifier.padding(20.dp,0.dp,0.dp,0.dp),
            text = ip,
            color = Color.DarkGray,
        )
    }
}

@Composable
fun WirelessConnection(wifi: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(10.dp,0.dp),
            text = "Wireless:",
            color = Color.Black,
        )
        Text(
            modifier = Modifier.padding(21.dp,0.dp,0.dp,0.dp),
            text = wifi,
            color = Color.DarkGray,
        )
    }
}

@Composable
fun Port () {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 5.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(10.dp, 0.dp),
            text = "Port:",
            color = Color.Black,
        )
//        TextField(
//            modifier = Modifier.padding(21.dp, 0.dp, 0.dp, 0.dp).width(80.dp),
//            TextFieldColors = Color.DarkGray,
//            value = port,
//            onValueChange = {newText ->
//                port = newText},
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//            singleLine = true
//        )
    }
}
