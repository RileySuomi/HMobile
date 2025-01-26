package com.example.demorobocontrollerapp

import android.content.res.Configuration
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.demorobocontrollerapp.ui.theme.DemoRoboControllerAppTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

//use as 'preview'
@Preview(showBackground = true)
@Composable
fun LoadingPreview() {
    DemoRoboControllerAppTheme {
        DisplayLoading() // pass in the 'viewModel' class
    }
}

@Composable
fun DisplayLoading(){
    val configuration = LocalConfiguration.current // check view mode
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    Box(
        modifier = Modifier.fillMaxSize().background(Color.White)
    ){
        if (isLandscape){
            Text(
                text = "HMobile",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center)
            )
        }else{
            Text(
                text = "HMobile",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Center).padding(0.dp,0.dp,0.dp,50.dp)
            )
        }

        Text(
            text = "by RoboRangers",
            fontSize = 15.sp,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.BottomCenter).padding(0.dp, 50.dp)
        )
    }
}