package com.example.demorobocontrollerapp
import android.util.Log
import java.io.PrintWriter
import java.net.Socket
import kotlinx.coroutines.*
import java.io.IOException

// Model: data and business logic of the application.

data class RobotControllerModel(
    var displayMessage: String,
    var isPowerOn : Boolean,
)

class RobotControllerRepository{
    private var _robotData = RobotControllerModel("Left lift with ease",false)


    val displayMessage: String = _robotData.displayMessage
    var isPowerOn: Boolean = _robotData.isPowerOn

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
            socketConnection = Socket("10.0.2.2", 65432);
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
        writer?.write("Bye Bye!\n");
        socketConnection.close();
        writer?.flush();
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