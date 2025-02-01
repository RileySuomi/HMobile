package com.example.demorobocontrollerapp

import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

object WebSocketManager {
    private lateinit var webSocket: WebSocket
    // TODO: Change this to Raspberry PI port number
    private const val SERVER_URL = "ws://10.0.2.2:8765" // Temporarily Emulator's way to access local host (fancy was of saying when you run emulator)

    private val client = OkHttpClient.Builder().build()

    fun connect() {
        Log.d("WebSocket", "Attempting to connect to $SERVER_URL")

        val request = Request.Builder().url(SERVER_URL).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                Log.d("WebSocket(onOpen)", "Connected successfully!")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket(onMessage)", "Message received: $text")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                Log.e("WebSocket(onFailure)", "Connection failed: ${t.message}")
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket(onClosing)", "Closing WebSocket: $code $reason")
            }
        })
    }

    fun sendMessage(message: String) {
        if (WebSocketManager::webSocket.isInitialized) {
            Log.d("WebSocket", "Sending message: $message")
            webSocket.send(message)
        } else {
            Log.e("WebSocket", "WebSocket is not connected")
        }
    }

    fun disconnect() {
        if (WebSocketManager::webSocket.isInitialized) {
            Log.d("WebSocket", "Disconnecting WebSocket")
            webSocket.close(1000, "Disconnected")
        }
    }
}
