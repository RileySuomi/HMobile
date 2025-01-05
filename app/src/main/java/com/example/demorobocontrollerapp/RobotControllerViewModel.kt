// ViewModel: Manages UI-related data and interacts with the Model to provide it to the View.

package com.example.demorobocontrollerapp
import androidx.lifecycle.ViewModel

// This class define and hold data belong to a robot to keep data state &  prevent data reset when view mode changes
class RobotControllerViewModel : ViewModel() {
    private val _repository: RobotControllerRepository = RobotControllerRepository()
    // Private mutable state (encapsulated)
    private var _displayMessage = _repository.displayMessage
    private var _isPowerOn = _repository.isPowerOn // set to false by default

    // Publicly exposed immutable state
    val displayText: String = _displayMessage
    val isPowerOn: Boolean = _isPowerOn

    // Public method to update the display text
    fun switchPowerStatus(){
        _isPowerOn = !_isPowerOn
    }
    fun setDisplayText(newDisplayMessage: String) {
        _repository.setDisplayMessage(newDisplayMessage)
    }
}

// implement negation - !
private operator fun Unit.not() {
    return !this
}
