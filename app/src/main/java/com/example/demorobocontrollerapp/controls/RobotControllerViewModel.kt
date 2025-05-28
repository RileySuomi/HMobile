// ViewModel: Manages UI-related data and interacts with the Model to provide it to the View.
package com.example.demorobocontrollerapp.controls
import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.demorobocontrollerapp.data.RobotInfoRepository
import com.example.demorobocontrollerapp.data.source.local.settings.prefversion.DataStoreRepo
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.GrabberInstruction
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

@HiltViewModel
class RobotControllerViewModel @Inject constructor(
    private val dataStore: DataStoreRepo,
    private val robotRepository: RobotInfoRepository
): ViewModel() {
    val webSocketManager =
        WebSocketClient // com.example.demorobocontrollerapp.WebSocketManager is initialized

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

    private var _isAdvancedMode = MutableStateFlow(false)
    val isAdvancedMode = _isAdvancedMode.asStateFlow()

    val _mapBitmap = MutableStateFlow<Bitmap?>(null)
    var mapBitmap: Flow<Bitmap?> = _mapBitmap

    data class PacketInfo(
        val id: String,
        val parameters: List<String>,
        val help: String
    )


    // Packet Spinner
    var packetList = listOf(
        "Movement",
        "Grabber Instruction",
        "Constant Communication",
        "Grabber Information",
        "Movement Response",
        "Debug"
    )
    val packetData = mapOf(
        "Movement" to PacketInfo(
            id = "Movement",
            parameters = listOf("Speed", "Angular"),
            help = "..."
        ),
        "Grabber Instruction" to PacketInfo(
            id = "GrabberInstruction",
            parameters = listOf("Height", "Status"),
            help = "..."
        ),
    )


    fun toggleAdvancedMode() {
        viewModelScope.launch {
            val toggleMode = !_isAdvancedMode.value
            _isAdvancedMode.value = toggleMode
        }
    }

    private var connection = RobotConnection();


    // Public method to update the display text
    fun switchPowerStatus() {
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
    private val _selectedPacket = MutableStateFlow<String>(packetList[0])
    private val _currPacketInfo = mutableStateOf(packetData[_selectedPacket.value])
    var currPacket = 0

    fun onSelectPacket(index: Int) {
        _selectedPacket.value = packetList[index]
        _currPacketInfo.value = packetData[_selectedPacket.value]
        currPacket = index
    }

    fun getParameter(): List<String>? {
        return _currPacketInfo.value?.parameters
    }

    fun getInstruct(): String? {
        return _currPacketInfo.value?.help
    }


    fun getId(): String? {
        return _currPacketInfo.value?.id
    }

    fun displayDialog(message: String) {
        _dialogMessage.value = message
        _showDialog.value = true
        CoroutineScope(Dispatchers.Main).launch {
            delay(2000)
            _showDialog.value = false
        }
    }

    fun openConnection() {

    }

    fun hardRotationRight() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Rotation", "Send hard rotation right")
            robotRepository.sendMovement(0.5f, 1.57f)
        }
    }

    fun hardRotationLeft() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d("Rotation", "Send hard rotation left")
            robotRepository.sendMovement(0.5f, -1.57f)
        }
    }

    fun startCommunication() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.beginCommunication()
            delay(1000)
            listenForMap()
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

    fun lift() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.sendLiftLower(-1.0f)
        }
    }

    fun lower() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.sendLiftLower(1.0f)
        }
    }


    fun grab() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.sendGrabber(GrabberInstruction.Close)
        }

    }

    fun release() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.sendGrabber(GrabberInstruction.Open)
        }
    }

    fun extend() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.sendExtendRetract(-1.0f)
        }
    }

    fun retract() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.sendExtendRetract(1.0f)
        }
    }

    fun getMap() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.sendMapRequest()
        }
    }

    private fun listenForMap() {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.getMapState().collect { bitmap ->
                _mapBitmap.value = bitmap
            }
        }
    }

    fun sendZeroLift(){
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.sendZeroMovementLift(0f)
        }
    }

    fun sendZeroRetract(){
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.sendZeroMovementRetract(0f)
        }
    }

    fun sendClickedCoordinate(mapX: Int, mapY: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            robotRepository.getMapMetadata()?.let { metadata ->
                val (worldX, worldY) = convertToWorldCoordinates(
                    mapX, mapY,
                    resolution = metadata.resolution,
                    originX = metadata.originX,
                    originY = metadata.originY,
                    mapHeight = metadata.height
                )

                robotRepository.sendCoordinates(worldX, worldY)
            } ?: run {
                Log.e("sendClickCoordinate", "Map metadata is not available")
            }

        }
}

    private fun convertToWorldCoordinates(
        pixelX: Int,
        pixelY: Int,
        resolution: Float,
        originX: Float,
        originY: Float,
        mapHeight: Int
    ): Pair<Float, Float> {
        // Flip y-axis again to align with ROS's bottom-left origin
        val mapY = mapHeight - pixelY

        val worldX = originX + pixelX * resolution
        val worldY = originY + mapY * resolution
        return Pair(worldX, worldY)
    }
}

    // implement negation - !
private operator fun Unit.not() {
    return !this
}