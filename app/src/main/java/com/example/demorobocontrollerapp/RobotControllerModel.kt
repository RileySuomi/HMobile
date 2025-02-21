package com.example.demorobocontrollerapp

import android.util.Log
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

// Model: data and business logic of the application.

data class RobotControllerModel(
    var displayMessage: String,
    var isPowerOn : Boolean,
    var usingJoystick: Boolean
)

data class JoystickModel (
    var width: Float,
    var length: Float,

    var baseRadius: Float = Math.min(width, length) / 3,
    var hatRadius: Float = Math.min(width, length) / 5,
    var capSize: Float = width / 4,
    var centerX: Float = width / 2,
    var centerY: Float = length / 2


)

class RobotControllerRepository{
    private var _robotData = RobotControllerModel("\"<camera stream>\"",false, false)
    var joystick = JoystickModel(1f, 1f)

    val displayMessage: String = _robotData.displayMessage
    var isPowerOn: Boolean = _robotData.isPowerOn
    var usingJoystick: Boolean = _robotData.usingJoystick

    fun setDisplayMessage(newDisplayMessage: String){
        _robotData.displayMessage = newDisplayMessage
    }
}

class RobotConnection {
    final var socketConnection = Socket();
    final var writer: PrintWriter? = null;
    private var openConnection = false;

    private var panicking = false;

    fun startConnection() {
        try {
            socketConnection = Socket("72.233.179.204", 65432);
            val stream = socketConnection.getOutputStream()
            writer = PrintWriter(stream);
        }
        catch (e: IOException) {
            Log.d("RobotController","Exception");
        }

        writer?.write("First message!\n");
        writer?.flush();
        openConnection = true;
    }

    fun EndConnection() {
        writer?.write("disconnect\n");
        writer?.flush();
        socketConnection.close();
        writer?.close();
        openConnection = false;
    }

    fun SendMessage(msg: String) {
        if (socketConnection.isConnected && !panicking) {
            writer?.write(msg);
            writer?.flush();
        }
    }

    fun Disconnect() {
        socketConnection.close();
    }

    fun Panick() {
        panicking = true;
    }

    fun EndPanick() {
        panicking = false;
    }
}