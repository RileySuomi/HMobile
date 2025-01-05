// ViewModel: Manages UI-related data and interacts with the Model to provide it to the View.

package com.example.demorobocontrollerapp
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

// This class define and hold data belong to a robot to keep data state &  prevent data reset when view mode changes
class RobotControllerViewModel : ViewModel() {
    // Private mutable state (encapsulated)
    private val _repository: RobotControllerRepository = RobotControllerRepository()
    private var _displayMessage = mutableStateOf(_repository.displayMessage)
    private var _isPowerOn = mutableStateOf(_repository.isPowerOn) // set to false by default

    // Publicly exposed immutable state
    val displayText: State<String> = _displayMessage
    val isPowerOn: State<Boolean> = _isPowerOn

    // Public method to update the display text
    fun switchPowerStatus(){
        _repository.isPowerOn = !_repository.isPowerOn
        _isPowerOn.value = _repository.isPowerOn
    }

    fun setDisplayText(newDisplayMessage: String) {
        _repository.setDisplayMessage(newDisplayMessage)
        _displayMessage.value = newDisplayMessage // update the state
    }
}

// implement negation - !
private operator fun Unit.not() {
    return !this
}
