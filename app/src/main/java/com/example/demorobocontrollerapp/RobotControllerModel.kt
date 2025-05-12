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
                socketConnection = Socket("72.233.179.206", 65432)
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

    fun listenForMapUpdates(onMapReceived: (Bitmap) -> Unit) {
        if (reader == null) return

        Thread {
            try {
                while (true) {
                    val jsonString = reader?.readLine() ?: continue
                    val json = JSONObject(jsonString)

                    val width = json.getInt("width")
                    val height = json.getInt("height")
                    val dataArray = json.getJSONArray("data")

                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    for (y in 0 until height) {
                        for (x in 0 until width) {
                            val i = (height - 1 - y) * width + x  // flip y-axis
                            val value = dataArray.getInt(i)
                            val color = when {
                                value == -1 -> Color.GRAY       // unknown
                                value == 0 -> Color.WHITE       // free space
                                value == 100 -> Color.BLACK     // occupied
                                else -> Color.RED               // unexpected
                            }
                            bitmap.setPixel(x, y, color)  // Android's (0,0) is top-left
                        }
                    }

                    onMapReceived(bitmap)
                }
            } catch (e: Exception) {
                Log.e("RobotConnection", "Error while listening for map: ${e.message}")
            }
        }.start()
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