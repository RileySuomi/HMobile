package com.example.demorobocontrollerapp.settings

import android.provider.Settings
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demorobocontrollerapp.data.RobotInfoRepository
import com.example.demorobocontrollerapp.data.source.local.settings.prefversion.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.junit.Test
import javax.inject.Inject

data class SettingsProperty (
    var screenName: String = "Default ScreenName",
    val currentValue: String = "Default setting",
    val dataKey: String = "noData",
    val editable: Boolean = false
)

sealed interface SettingsModelUiState {
    object Loading: SettingsModelUiState
    data class Error(val throwable: Throwable) : SettingsModelUiState
    data class Success(val data: List<SettingsProperty>) : SettingsModelUiState
}

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val robotRepository: RobotInfoRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    val uiState: StateFlow<SettingsModelUiState> = robotRepository
        .getSettings().map {
            SettingsModelUiState.Success(
                data = it.map { settingItem ->
                    SettingsProperty(
                        settingItem.settingDisplayName,
                        settingItem.settingValue,
                        settingItem.settingName,
                        settingItem.editable
                    )
                }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SettingsModelUiState.Loading)

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

    fun updateById(id: String, newValue: String): Unit {
        viewModelScope.launch {
            robotRepository.updateSetting(id, newValue)
        }
    }

    private fun loadData() {
        viewModelScope.launch {

//            robotRepository.updateSettings("phoneIp", "Phone IP", "Newer better value")
//            robotRepository.updateSettings("robotIp", "Robot IP", "Don't ask")
//            robotRepository.updateSettings("test", "Yooooooo!", "Don't ask")
//            robotRepository.updateSettings("test", "Very decent settings value", "Indeed")


            _phoneIp.value = (robotRepository.getSetting("phone_ip") ?: "").toString()
            _robotIp.value = (robotRepository.getSetting("robot_ip") ?: "").toString()
            _wifiName.value = (robotRepository.getSetting("wifi_name") ?: "").toString()
            _portNumber.value = (robotRepository.getSetting("port_number") ?: "").toString()
//            robotRepository.getSetting("advanced_mode").collect { advancedMode ->
//                _isAdvancedMode.value = advancedMode.toBoolean()
//            }
        }
    }

//    fun savePhoneIp(ip: String) {
//        viewModelScope.launch {
//            robotRepository.updateSettings("phone_ip", ip)
//            _phoneIp.value = ip
//        }
//    }
//
//    fun saveRobotIp(ip: String) {
//        viewModelScope.launch {
//            robotRepository.updateSettings("robot_ip", ip)
//            _robotIp.value = ip
//        }
//    }
//
//    fun saveWifiName(name: String) {
//        viewModelScope.launch {
//            robotRepository.updateSettings("wifi_name", name)
//            _wifiName.value = name
//        }
//    }
//
//    fun savePortNumber(port: String) {
//        viewModelScope.launch {
//            robotRepository.updateSettings("port_number", port)
//            _portNumber.value = port
//        }
//    }
//
//    fun toggleAdvancedMode(enabled: Boolean) {
//        viewModelScope.launch {
//            robotRepository.updateSettings("advanced_mode", enabled.toString())
//            _isAdvancedMode.value = enabled
//        }
//    }
}