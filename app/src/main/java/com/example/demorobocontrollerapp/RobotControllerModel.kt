package com.example.demorobocontrollerapp
// this is mergi check
// Model: data and business logic of the application.

data class RobotControllerModel(
    var displayMessage: String,
    var isPowerOn : Boolean,
)

class RobotControllerRepository{
    private var _robotData = RobotControllerModel("\"<camera stream>\"",false)

    val displayMessage: String = _robotData.displayMessage
    var isPowerOn: Boolean = _robotData.isPowerOn

    fun setDisplayMessage(newDisplayMessage: String){
        _robotData.displayMessage = newDisplayMessage
    }
}