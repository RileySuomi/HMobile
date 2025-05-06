// ViewModel: Manages UI-related data and interacts with the Model to provide it to the View.
package com.example.demorobocontrollerapp.controls
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demorobocontrollerapp.data.source.local.settings.prefversion.DataStoreRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.example.demorobocontrollerapp.data.source.network.unused.WebSocketClient

@HiltViewModel
class RobotControllerViewModel @Inject constructor(
    private val dataStore: DataStoreRepo
): ViewModel() {
    val webSocketManager = WebSocketClient // com.example.demorobocontrollerapp.WebSocketManager is initialized

    // TODO: data to send through Raspberry Pi
    private val _robotState = MutableLiveData<String>()
    val robotState: LiveData<String> get() = _robotState

    // Private mutable state (encapsulated)
    private val _repository: RobotControllerRepository = RobotControllerRepository()
    private var _displayMessage = mutableStateOf(_repository.displayMessage)
    private var _isPowerOn = mutableStateOf(_repository.isPowerOn) // set to false by default
    private var _usingJoystick = mutableStateOf(_repository.usingJoystick)

    // these belong to this class / viewModel
    private val _showDialog = mutableStateOf(false) // alert dialog to state that power is off
    private val _dialogMessage = mutableStateOf("")
    val showDialog: State<Boolean> get() = _showDialog
    val dialogMessage: State<String> get() = _dialogMessage

    var joystick = mutableStateOf(_repository.joystick)

    // Publicly exposed immutable state
    val displayText: State<String> = _displayMessage
    val isPowerOn: State<Boolean> = _isPowerOn
    val usingJoystick: State<Boolean> = _usingJoystick

    // Access to DataStore

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

    fun toggleAdvancedMode() {
        viewModelScope.launch {
            val currentMode = isAdvancedMode.value
            dataStore.putBoolean("advanced_mode", !currentMode)
        }
    }

    private var connection = RobotConnection();


    // Public method to update the display text
    fun switchPowerStatus(){
        _repository.isPowerOn = !_repository.isPowerOn
        _isPowerOn.value = _repository.isPowerOn
    }

    fun switchJoystick() {
        _repository.usingJoystick = !_repository.usingJoystick
        _usingJoystick.value = _repository.usingJoystick
    }

    fun setDisplayText(newDisplayMessage: String) {
        _repository.setDisplayMessage(newDisplayMessage)
        _displayMessage.value = newDisplayMessage // update the state
    }

    //log display
    private val _logLines = MutableStateFlow<List<String>>(emptyList())
    val logLines: StateFlow<List<String>> = _logLines.asStateFlow()

    fun addLogMessage(message: String) {
        _logLines.value += message
    }

    //packet spinner
    val packetList = _repository.packetList
    private val _selectedPacket = MutableStateFlow<String>(packetList[0])
    private val _currPacketInfo = mutableStateOf(_repository.packetData[_selectedPacket.value]?: PacketInfo(emptyList(), "No instruction found."))
    val currPacketInfo: State<PacketInfo> get() = _currPacketInfo

    fun onSelectPacket(packet: String){
        _selectedPacket.value = packet
        _currPacketInfo.value = _repository.packetData[_selectedPacket.value]?: PacketInfo(emptyList(), "No instruction found.")
    }
    fun getParameter(): List<String>{
        return currPacketInfo.value.parameters
    }
    fun getInstruct(): String{
        return currPacketInfo.value.help
    }

    fun displayDialog(message: String){
        _dialogMessage.value = message
        _showDialog.value = true
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            _showDialog.value = false
        }
    }
    fun startCommunication() {
        viewModelScope.launch(Dispatchers.IO) {
            connection.startConnection();
        }
    }

    fun endCommunication() {
        viewModelScope.launch(Dispatchers.IO) {
            connection.EndConnection();
        }
    }

    fun moveUp() {
        viewModelScope.launch(Dispatchers.IO) {
            connection.SendMessage("forward");
            Log.d("ViewModel", "Up message sent!")
        }
    }

    fun moveDown() {
        viewModelScope.launch(Dispatchers.IO) {
            connection.SendMessage("backward");
        }
    }

    fun moveLeft() {
        viewModelScope.launch(Dispatchers.IO) {
            connection.SendMessage("Left\n");
        }
    }

    fun moveRight() {
        viewModelScope.launch(Dispatchers.IO) {
            connection.SendMessage("Right\n");
        }
    }
}

// implement negation - !
private operator fun Unit.not() {
    return !this
}