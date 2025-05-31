package com.example.demorobocontrollerapp

import android.app.Application
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.demorobocontrollerapp.controls.RobotControllerViewModel
import com.example.demorobocontrollerapp.data.RobotInfoRepository
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application(){
    override fun onCreate() {
        super.onCreate();
    }
}