package com.example.demorobocontrollerapp.controls

import android.util.Log
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket

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

enum class GrabberInstruction{CLOSE, OPEN, IDLE}
//enum class GrabberStatus{CLOSING, OPENING, CLOSED, OPENED}
//enum class GrabberMovement{MOVING_UP, MOVING_DOWN, IDLE}

sealed class Packets {
    data class Movement(
        val id: String = "Movement",
        val speed: Double,
        val angular: Double
    ) : Packets()

    data class GrabberIns(
        val id: String = "Grabber Instruction",
        val height: Double,
        val status: GrabberInstruction
    ) : Packets()

    data class ConstantComm(
        val id: String = "Constant Communication",
//        var battery: Double = 0.0,
//        var temp: Double = 0.0
    ) : Packets()

    data class GrabberInfo(
        val id: String = "Grabber Information",
//        var height: Double = 0.0,
//        var status: GrabberStatus.,
//        var movement: GrabberMovement
    ) : Packets()

    data class MovementResp(
        val id: String = "Movement Response",
//        var command: String,
//        var speed: Float
    ) : Packets()

    data class Debug(
        val id: String = "Debug",
    ) : Packets()
}

class RobotControllerRepository{
    private var _robotData = RobotControllerModel("<camera offline>",false, false)
    var joystick = JoystickModel(1f, 1f)

    val displayMessage: String = _robotData.displayMessage
    var isPowerOn: Boolean = _robotData.isPowerOn
    var usingJoystick: Boolean = _robotData.usingJoystick

    var packetList = listOf("Movement", "Grabber Instruction", "Constant Communication", "Grabber Information", "Movement Response", "Debug")

    fun setDisplayMessage(newDisplayMessage: String){
        _robotData.displayMessage = newDisplayMessage
    }
}

class RobotConnection {
    var socketConnection = Socket()
    var writer: PrintWriter? = null
    private var openConnection = false
    private var panicking = false

    fun startConnection(ip: String = "72.233.179.204") {
        try {
            socketConnection = Socket(ip, 65432)
            val stream = socketConnection.getOutputStream()
            writer = PrintWriter(stream)
        }
        catch (e: IOException) {
            Log.d("RobotController","Exception")
        }

        writer?.write("First message!\n")
        writer?.flush()
        openConnection = true
    }

    fun EndConnection() {
        writer?.write("disconnect\n")
        writer?.flush()
        socketConnection.close()
        writer?.close()
        openConnection = false
    }

    fun SendMessage(msg: String) {
        if (socketConnection.isConnected && !panicking) {
            writer?.write(msg)
            writer?.flush()
        }
    }

    fun Disconnect() {
        socketConnection.close()
    }

    fun Panick() {
        panicking = true
    }

    fun EndPanick() {
        panicking = false
    }
}