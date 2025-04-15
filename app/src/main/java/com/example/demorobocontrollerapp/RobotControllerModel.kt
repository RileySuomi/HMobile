package com.example.demorobocontrollerapp

import android.util.Log
import org.json.JSONArray
import java.io.IOException
import java.io.PrintWriter
import java.net.Socket
import org.json.JSONObject
import android.graphics.Bitmap
import android.graphics.Color
import android.widget.ImageView
import java.io.BufferedReader
import java.io.InputStreamReader

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
    private var _robotData = RobotControllerModel("<camera offline>",false, false)
    var joystick = JoystickModel(1f, 1f)

    val displayMessage: String = _robotData.displayMessage
    var isPowerOn: Boolean = _robotData.isPowerOn
    var usingJoystick: Boolean = _robotData.usingJoystick

    fun setDisplayMessage(newDisplayMessage: String){
        _robotData.displayMessage = newDisplayMessage
    }
}

class RobotConnection {
    var socketConnection = Socket()
    var writer: PrintWriter? = null
    var reader: BufferedReader? = null
    private var openConnection = false
    private var isListening = false

    private var panicking = false

    fun startConnection() {
        Thread {
            try {
                socketConnection = Socket("72.233.179.204", 65432)
                val stream = socketConnection.getOutputStream()
                writer = PrintWriter(stream)
                reader = BufferedReader(InputStreamReader(socketConnection.getInputStream()))

                writer?.write("First message!\n")
                writer?.flush()
                openConnection = true

            } catch (e: IOException) {
                Log.d("RobotController", "Exception")
            }
        }.start()
    }

    fun startListeningForMap(imageView: ImageView) {
        if (!openConnection) {
            Log.e("RobotConnection", "Not connected to the server!")
            return
        }
        if (isListening) {
            Log.d("RobotConnection", "Already listening for map data")
            return
        }

        isListening = true

        Thread {
            listenForMapData(imageView)  // Call the private function safely
        }.start()
    }

    private fun listenForMapData(imageView: ImageView) {
        Thread {
            try {
                val inputStream = socketConnection.getInputStream()
                val reader = BufferedReader(InputStreamReader(inputStream))

                while (true) {
                    val line = reader.readLine()
                    if (line != null) {
                        val json = JSONObject(line)
                        val width = json.getInt("width")
                        val height = json.getInt("height")
                        val resolution = json.getDouble("resolution")
                        val data = json.getJSONArray("data")

                        Log.d("RobotConnection", "Received Map: ${width}x${height}, Resolution: $resolution")
                        updateMapUI(width, height, data, imageView)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }


    private fun updateMapUI(width: Int, height: Int, data: JSONArray, imageView: ImageView) {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        for (y in 0 until height) {
            for (x in 0 until width) {
                val index = y * width + x
                val value = data.getInt(index) // Get occupancy value (-1, 0-100)

                // Convert occupancy value to a grayscale color
                val color = when {
                    value == -1 -> Color.GRAY  // Unknown area
                    value == 0 -> Color.WHITE  // Free space
                    value in 1..100 -> Color.BLACK // Occupied space
                    else -> Color.RED  // Should never happen
                }

                bitmap.setPixel(x, height - y - 1, color) // Flip vertically
            }
        }

        // Update the ImageView on the UI thread
        imageView.post {
            imageView.setImageBitmap(bitmap)
        }
    }

    fun requestMap() {
        if (openConnection && writer != null) {
            writer?.apply {
                write("GET_MAP\n")  // Send request with newline
                flush()
            }
            Log.d("RobotConnection", "Sent GET_MAP request")
        } else {
            Log.e("RobotConnection", "Connection is not open or writer is null")
        }
    }



    fun EndConnection() {
        writer?.write("disconnect\n")
        writer?.flush()
        socketConnection.close()
        writer?.close()
        reader?.close()
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