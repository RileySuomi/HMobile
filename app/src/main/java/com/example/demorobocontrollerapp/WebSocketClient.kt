package com.example.demorobocontrollerapp



// TODO: Update this file once the app is ready for production
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

// WebSocketClient class handles WebSocket operations
class WebSocketClient {

    private var webSocket: WebSocket? = null
    private val client = OkHttpClient()

    // Listener that handles WebSocket events (onOpen, onMessage, onFailure, etc.)
    val listener = object : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            super.onOpen(webSocket, response)
            Log.d("WebSocket", "Connected to server") // Connection success
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.d("WebSocket", "Message from server: $text") // Message received from server
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            super.onMessage(webSocket, bytes)
            Log.d("WebSocket", "Message from server: ${bytes.hex()}") // Message received as ByteString
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Log.d("WebSocket", "Closing: $reason") // Connection closing
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            super.onFailure(webSocket, t, response)
            Log.d("WebSocket", "Error: ${t.message}") // Error during WebSocket communication
        }
    }

    // Starts WebSocket connection to the server
    fun startWebSocketConnection(url: String) {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, listener) // Open connection
    }

    // Sends a message to the WebSocket server
    fun sendMessage(message: String) {
        webSocket?.send(message) // Send message over WebSocket
    }

    // Closes the WebSocket connection
    fun closeConnection() {
        webSocket?.close(1000, "Closing connection") // Close the WebSocket with code 1000 (normal closure)
    }
}

