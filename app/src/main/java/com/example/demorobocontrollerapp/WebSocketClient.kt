package com.example.demorobocontrollerapp

import android.content.Context
import android.util.Log
import okhttp3.Dns
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.net.InetAddress
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

object WebSocketClient {
    private var webSocket: WebSocket? = null
    private var client: OkHttpClient? = null

    fun connect(context: Context) {
        // Retrieve the SSL socket factory and trust manager from our SSLHelper using our certificate resource.
        val sslSocketFactory: SSLSocketFactory? = SSLHelper.getSocketFactory(context, R.raw.websocket)
        val trustManager: X509TrustManager? = SSLHelper.getTrustManager(context, R.raw.websocket)

        // demo only: val request = Request.Builder().url("ws://10.0.2.2:8765").build()
        if (sslSocketFactory != null && trustManager != null) {
            // Create a custom DNS resolver that maps the hostname "Robo" to the emulator's host IP (10.0.2.2).
            val customDns = object : Dns {
                override fun lookup(hostname: String): List<InetAddress> {
                    return if (hostname.equals("Robo", ignoreCase = true)) { // change 'Robo' to Raspberry PI later
                        listOf(InetAddress.getByName("10.0.2.2"))
                    } else {
                        Dns.SYSTEM.lookup(hostname)
                    }
                }
            }

            // Build the OkHttpClient with:
            // - Custom DNS to resolve "Robo"
            // - A hostname verifier that accepts "Robo" (matches our certificate)
            // - The SSL settings from our certificate.
            client = OkHttpClient.Builder()
                .dns(customDns)
                .hostnameVerifier { hostname, _ ->
                    // Verify that the hostname is "Robo"
                    hostname.equals("Robo", ignoreCase = true)
                }
                .sslSocketFactory(sslSocketFactory, trustManager)
                .build()

            // Create a WebSocket request using the hostname "Robo".
            val request = Request.Builder().url("wss://Robo:8765").build()

            // Connect to the WebSocket server and set up event listeners.
            webSocket = client?.newWebSocket(request, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                    Log.d("WebSocketManager", "WebSocket Opened: ${response.message}")
                    // Send an initial message when the connection is established.
                    webSocket.send("Hello, server!")
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    Log.d("WebSocketManager", "Received message: $text")
                }

                override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                    Log.d("WebSocketManager", "WebSocket closing: $code / $reason")
                    webSocket.close(code, reason)
                }

                override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                    Log.d("WebSocketManager", "WebSocket closed: $code / $reason")
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                    Log.e("WebSocketManager", "WebSocket error: ${t.message}", t)
                }
            })
        } else {
            Log.e("WebSocketManager", "Failed to set up SSL connection")
        }
    }

    // Send a message through the connected WebSocket.
    fun sendMessage(message: String) {
        webSocket?.send(message) ?: Log.e("WebSocketManager", "WebSocket is not connected.")
    }

    // Close the WebSocket connection gracefully.
    fun disconnect() {
        webSocket?.close(1000, "Closing Connection")
    }
}
