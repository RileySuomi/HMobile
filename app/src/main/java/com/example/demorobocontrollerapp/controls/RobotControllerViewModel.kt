// ViewModel: Manages UI-related data and interacts with the Model to provide it to the View.
package com.example.demorobocontrollerapp.controls
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import com.example.demorobocontrollerapp.data.RobotInfoRepository
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
import kotlinx.coroutines.flow.first

@HiltViewModel
class RobotControllerViewModel @Inject constructor(
    private val dataStore: DataStoreRepo,
    private val robotRepository: RobotInfoRepository
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

    private val _isAdvancedMode = MutableStateFlow(true)
    val isAdvancedMode = _isAdvancedMode.asStateFlow()

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

    fun openConnection() {

    }

    fun startCommunication() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.beginCommunication()
        }
    }

    fun endCommunication() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.endCommunication()
        }
    }

    fun moveUp() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.sendMovement(0.5f, 0f)
        }
    }

    fun moveDown() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.sendMovement(-0.5f, 0f)
        }
    }

    fun moveLeft() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.sendMovement(0.3f, 0.3f)
        }
    }

    fun moveRight() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.sendMovement(0.3f, -0.3f)
        }
    }
}

// implement negation - !
private operator fun Unit.not() {
    return !this
}