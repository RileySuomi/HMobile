// ViewModel: Manages UI-related data and interacts with the Model to provide it to the View.
package com.example.demorobocontrollerapp
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// This class define and hold data belong to a robot to keep data state &  prevent data reset when view mode changes
class RobotControllerViewModel : ViewModel() {
    val webSocketManager = WebSocketManager // com.example.demorobocontrollerapp.WebSocketManager is initialized

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
