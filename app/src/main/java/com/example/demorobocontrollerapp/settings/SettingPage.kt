@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.demorobocontrollerapp.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.demorobocontrollerapp.extracompose.CustomButton
import com.example.demorobocontrollerapp.data.source.local.datastore.DataStoreRepo
import com.example.demorobocontrollerapp.controls.RobotControllerViewModel
import com.example.demorobocontrollerapp.ui.theme.DemoRoboControllerAppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


//use as 'preview'
@Preview(showBackground = true)
@Composable
fun SettingPreview() {
    val navController = rememberNavController()

    DemoRoboControllerAppTheme {
        val fakeRepo = object : DataStoreRepo {
            override suspend fun putString(key: String, value: String) {}
            override suspend fun putBoolean(key: String, value: Boolean) {}
            override suspend fun getString(key: String): Flow<String>{
                return flow { emit("mocked_string_value")}
            }
            override suspend fun getBoolean(key: String): Flow<Boolean>{
                return flow { emit(false)}
            }
            override suspend fun clearPReferences(key: String) {}
        }

        val fakeViewModel = RobotControllerViewModel(fakeRepo)

        DisplaySetting(
            viewModel = fakeViewModel,
            onBackPressed = { navController.navigate("home") }
        )
    }
}

@Composable
fun DisplaySetting(viewModel: RobotControllerViewModel = hiltViewModel(), onBackPressed: () -> Unit) {
    val portFromViewModel by viewModel.portNumber.collectAsState()
    Scaffold(
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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Localized description"
                        )
                    }
                },
            )
        },

        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
                    .padding(0.dp, 10.dp, 0.dp, 10.dp)
            ) {
                RobotIP(viewModel.robotIp.toString())
                Divider(color = Color.LightGray)
                PhoneIP(viewModel.phoneIp.toString())
                Divider(color = Color.LightGray)
                WirelessConnection(viewModel.wifiName.toString())
                Divider(color = Color.LightGray)
                Port(
                    currentValue = portFromViewModel,
                    onSave = { newPort ->
                        viewModel.savePortNumber(newPort)
                    }
                )
//                Divider(color = Color.LightGray)
//                AdvMode()
            }
        }
    )
}

@Composable
fun PhoneIP(ip: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
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
fun RobotIP(ip: String) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.padding(10.dp,0.dp),
            text = "Robot IP:",
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
            .padding(0.dp, 10.dp),
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

//editable port
@Composable
fun Port (currentValue: String, onSave: (String) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier
                .padding(start = 10.dp)
                .weight(1f),
            text = "Port:",
            color = Color.Black,
        )
        Row(
            Modifier
                .weight(2f)
                .padding(0.dp),
            verticalAlignment = Alignment.CenterVertically
        )
        {
            var inputPort by remember { mutableStateOf(currentValue) }
            BasicTextField(
                modifier = Modifier
                    .weight(1f)
                    .background(Color.DarkGray)
                    .padding(bottom = 1.dp)
                    .background(Color.White),
                textStyle = LocalTextStyle.current.copy(fontSize = 18.sp),
                value = inputPort,
                onValueChange = { inputPort = it },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.padding(horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        innerTextField()
                    }
                }
            )

            Box(modifier = Modifier.weight(1f)){
                CustomButton(
                    text = if (currentValue != inputPort) "Save" else "Saved",
                    isEnabled = (currentValue != inputPort),
                    padding = PaddingValues(start = 15.dp),
                    onClick = {onSave(inputPort)}
                )
            }
        }
    }
}

//advanced mode switch
//@Composable
//fun AdvMode(state: MutableState<Boolean> = remember { mutableStateOf(false) }){
//    Row(
//        modifier = Modifier.fillMaxWidth(),
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center
//    ){
//        Text(
//            modifier = Modifier.padding(end = 20.dp),
//            text = "Advanced Mode"
//        )
//        Switch(
//            checked = state.value,
//            onCheckedChange = {
//                state.value = it
//                //add advanced display sheet (collapsable) / mask on top of screen
//            },
//            thumbContent = if (state.value) {
//                {
//                    Icon(
//                        imageVector = Icons.Filled.Check,
//                        "",
//                        Modifier.size(SwitchDefaults.IconSize),
//                        tint = Color.White
//                    )
//                }
//            }else{
//                null
//            },
//            colors = SwitchDefaults.colors(
//                checkedThumbColor = MaterialTheme.colorScheme.primary,
//                checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
//                uncheckedThumbColor = MaterialTheme.colorScheme.secondary,
//                uncheckedTrackColor = MaterialTheme.colorScheme.secondaryContainer,
//            )
//        )
//    }
//}