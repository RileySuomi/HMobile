package com.example.demorobocontrollerapp.data.source.network.tcpdatarequests

import android.util.Log
import org.json.JSONObject
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket
import javax.inject.Inject

class RobotNetworkDataSource @Inject constructor() : NetworkResultDataSource {
    private lateinit var socketConnection: Socket
    private var writer: PrintWriter? = null
    private var openConnection = false
    private var panicking = false

    private fun sendMessage(msg: String) {
        if (socketConnection.isConnected && !panicking) {
            writer?.write(msg)
            writer?.flush()
        }
    }

    private fun startConnection() {
        try {
            socketConnection = Socket("72.233.179.204", 65432)
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

    private fun checkNetworkStart() {
        if (!openConnection) {
            startConnection()
        }

    }

    override fun sendMovement(speed: Float, angular: Float) {
        checkNetworkStart()
        val move = NetworkMovementInstruction(speed, angular)
        sendMessage(move.toString())
    }

    override fun sendGrabber(value: Int, grabber: GrabberInstruction) {
        checkNetworkStart()
        val grab = NetworkGrabberInstruction(value, grabber)
        sendMessage(grab.toString())
    }

    override fun updateRobotStatus() {
        checkNetworkStart()

    }

    override fun endConnections() {
        endConnection()
    }

    private fun endConnection() {
        writer?.write("disconnect\n")
        writer?.flush()
        socketConnection.close()
        writer?.close()
        openConnection = false
    }

}