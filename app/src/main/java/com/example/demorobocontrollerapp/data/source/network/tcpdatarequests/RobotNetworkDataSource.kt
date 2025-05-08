package com.example.demorobocontrollerapp.data.source.network.tcpdatarequests

import android.util.Log
import org.json.JSONObject
import java.io.IOException
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import javax.inject.Inject
import javax.net.SocketFactory
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

class RobotNetworkDataSource @Inject constructor() : NetworkResultDataSource {
    private var socketConnection: Socket = Socket()
    private var writer: PrintWriter? = null
    private var openConnection = false
    private var panicking = false
    private var insecure = false

    var defaultHost: String = "10.10.10.10"
    var defaultPort: Int = 65432

    private fun sendMessage(msg: String) {
        if (socketConnection.isConnected && !panicking) {
            writer?.write(msg)
            writer?.flush()
        }
    }


    override fun startConnection(host: String, port: Int) {
        try {
            val context = SSLSocketFactory.getDefault()
            //val underConnection = Socket(host, port, )
            defaultHost = host
            defaultPort = port

            socketConnection.connect(InetSocketAddress(host, port), 5000)//context.createSocket(host, port)
            val stream = socketConnection.getOutputStream()

            writer = PrintWriter(stream)

            writer?.write("First message!\n")
            writer?.flush()
            openConnection = true
            insecure = true
        }
        catch (e: IOException) {
            Log.d("RobotController",e.toString())
        }


    }

    private fun checkNetworkStart() {
        if (!openConnection) {
            startConnection(defaultHost, defaultPort)
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
        socketConnection = Socket()
        writer?.close()
        openConnection = false
    }

}