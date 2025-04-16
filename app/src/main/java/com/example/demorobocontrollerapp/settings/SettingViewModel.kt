package com.example.demorobocontrollerapp.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demorobocontrollerapp.data.source.local.datastore.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val dataStore: DataStoreRepo
): ViewModel() {
    private val _phoneIp = MutableStateFlow("")
    val phoneIp = _phoneIp.asStateFlow()

    private val _robotIp = MutableStateFlow("")
    val robotIp = _robotIp.asStateFlow()

    private val _wifiName = MutableStateFlow("")
    val wifiName = _wifiName.asStateFlow()

    private val _portNumber = MutableStateFlow("")
    val portNumber = _portNumber.asStateFlow()

    private val _isAdvancedMode = MutableStateFlow(false)
    val isAdvancedMode = _isAdvancedMode.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _phoneIp.value = (dataStore.getString("phone_ip") ?: "").toString()
            _robotIp.value = (dataStore.getString("robot_ip") ?: "").toString()
            _wifiName.value = (dataStore.getString("wifi_name") ?: "").toString()
            _portNumber.value = (dataStore.getString("port_number") ?: "").toString()
            dataStore.getBoolean("advanced_mode").collect { advancedMode ->
                _isAdvancedMode.value = advancedMode
            }
        }
    }

    fun savePhoneIp(ip: String) {
        viewModelScope.launch {
            dataStore.putString("phone_ip", ip)
            _phoneIp.value = ip
        }
    }

    fun saveRobotIp(ip: String) {
        viewModelScope.launch {
            dataStore.putString("robot_ip", ip)
            _robotIp.value = ip
        }
    }

    fun saveWifiName(name: String) {
        viewModelScope.launch {
            dataStore.putString("wifi_name", name)
            _wifiName.value = name
        }
    }

    fun savePortNumber(port: String) {
        viewModelScope.launch {
            dataStore.putString("port_number", port)
            _portNumber.value = port
        }
    }

    fun toggleAdvancedMode(enabled: Boolean) {
        viewModelScope.launch {
            dataStore.putBoolean("advanced_mode", enabled)
            _isAdvancedMode.value = enabled
        }
    }
}