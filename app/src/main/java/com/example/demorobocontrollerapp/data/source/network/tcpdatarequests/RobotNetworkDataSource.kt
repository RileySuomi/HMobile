package com.example.demorobocontrollerapp.data.source.network.tcpdatarequests

import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import javax.inject.Inject
import javax.net.SocketFactory
import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory
import android.graphics.Bitmap.Config
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.MapMetadata

class RobotNetworkDataSource @Inject constructor() : NetworkResultDataSource {
    private var socketConnection: Socket = Socket()
    private var writer: PrintWriter? = null
    private var reader: BufferedReader? = null
    private var openConnection = false
    private var panicking = false
    private var insecure = false
    private val gson = Gson()
    override var currentMap = createBitmap(1,1)
    override var mapMetadata: MapMetadata? = null

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
        val movementElement: JsonObject = gson.toJsonTree(move) as JsonObject
        movementElement.addProperty("type", "movement")
        sendMessage(gson.toJson(move) + "\n")
    }

    override fun sendLifter(value: Float) {
        checkNetworkStart()
        var movementElement: JsonObject = gson.toJsonTree(NetworkLiftInstruction(value)) as JsonObject
        movementElement.addProperty("type", "elevator")
        sendMessage(gson.toJson(movementElement) + "\n")
    }

    override fun sendGrabber(value: GrabberInstruction) {
        checkNetworkStart()
        val grab: JsonObject = gson.toJsonTree(NetworkGrabberInstruction(value)) as JsonObject
        sendMessage(gson.toJson(grab) + "\n")
    }

    override fun sendMapRequest() {
        checkNetworkStart()
        sendMessage("get_map")
    }

    override fun listenForMap() {
        if (reader == null) return

        Thread {
            try {
                while (true) {
                    val jsonString = reader?.readLine() ?: continue
                    val json = JSONObject(jsonString)

                    val width = json.getInt("width")
                    val height = json.getInt("height")
                    val resolution = json.getDouble("resolution").toFloat()
                    val origin = json.getJSONObject("origin")
                    val originX = origin.getDouble("x").toFloat()
                    val originY = origin.getDouble("y").toFloat()

                    mapMetadata = MapMetadata(resolution, originX, originY, width, height)

                    val dataArray = json.getJSONArray("data")

                    val bitmap = createBitmap(width, height)
                    for (y in 0 until height) {
                        for (x in 0 until width) {
                            val i = (height - 1 - y) * width + x  // flip y-axis
                            val value = dataArray.getInt(i)
                            val color = when (value) {
                                -1 -> Color.GRAY       // unknown
                                0 -> Color.WHITE       // free space
                                100 -> Color.BLACK     // occupied
                                else -> Color.RED               // unexpected
                            }
                            bitmap[x, y] = color  // Android's (0,0) is top-left
                        }
                    }

                    currentMap = bitmap
                }
            } catch (e: Exception) {
                Log.e("RobotConnection", "Error while listening for map: ${e.message}")
            }
        }.start()
    }

    override fun sendCoordinates(xCoordinate: Float, yCoordinate: Float){
        checkNetworkStart()
        val goal = JsonObject().apply {
            addProperty("type", "goal")
            addProperty("x", xCoordinate)
            addProperty("y", yCoordinate)
        }
        sendMessage(gson.toJson(goal) + "\n")
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