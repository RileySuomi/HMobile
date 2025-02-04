package com.example.demorobocontrollerapp

import android.content.Context
import android.content.Context.WIFI_SERVICE
import android.net.ConnectivityManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.net.wifi.WifiManager
import android.text.format.Formatter.formatIpAddress
import androidx.core.content.ContextCompat.getSystemService
import java.util.Formatter

class SettingViewModel : ViewModel(){
    private val _deviceIP = MutableLiveData<String>()
    val deviceIP: LiveData<String> = _deviceIP

    private val _wifiName = MutableLiveData<String>()
    val wifiName: LiveData<String> = _wifiName

    private val _portCode = MutableLiveData<String>()
    val portCode: LiveData<String> = _portCode

//    init {
//        fetchDeviceIP()
//        fetchWifiName()
//    }
//
//    fun updatePortCode(newPortCode: Code){
//        _portCode.value = newPortCode
//    }
//
//    private fun fetchDeviceIP(){
//        val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
//        _deviceIP.value = formatIpAddress(wifiManager.connectionInfo.ipAddress)
//    }
}