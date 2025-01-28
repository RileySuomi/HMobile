// ViewModel: Manages UI-related data and interacts with the Model to provide it to the View.

package com.example.demorobocontrollerapp
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// This class define and hold data belong to a robot to keep data state &  prevent data reset when view mode changes
class RobotControllerViewModel : ViewModel() {
    // Private mutable state (encapsulated)
    private val _repository: RobotControllerRepository = RobotControllerRepository()
    private var _displayMessage = mutableStateOf(_repository.displayMessage)
    private var _isPowerOn = mutableStateOf(_repository.isPowerOn) // set to false by default

    // these belong to this class / viewModel
    private val _showDialog = mutableStateOf(false) // alert dialog to state that power is off
    private val _dialogMessage = mutableStateOf("App is off")
    val showDialog: State<Boolean> get() = _showDialog
    val dialogMessage: State<String> get() = _dialogMessage
    private var connection = RobotConnection();

    // Publicly exposed immutable state
    val displayText: State<String> = _displayMessage
    val isPowerOn: State<Boolean> = _isPowerOn

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
            connection.SendMessage("Up\n");
            Log.d("ViewModel", "Up message sent!")
        }
    }

    fun moveDown() {
        viewModelScope.launch(Dispatchers.IO) {
            connection.SendMessage("Down\n");
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

    // Public method to update the display text
    fun switchPowerStatus(){
        _repository.isPowerOn = !_repository.isPowerOn
        _isPowerOn.value = _repository.isPowerOn
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
}

// implement negation - !
private operator fun Unit.not() {
    return !this
}
