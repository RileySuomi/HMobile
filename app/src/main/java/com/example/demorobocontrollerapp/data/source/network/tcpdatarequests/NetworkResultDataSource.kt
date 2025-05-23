package com.example.demorobocontrollerapp.data.source.network.tcpdatarequests

import android.graphics.Bitmap

interface NetworkResultDataSource {
    enum class GrabberStatus
    var currentMap: Bitmap

    fun startConnection(host: String, port: Int)

    fun sendMovement(speed: Float, angular: Float)

    fun sendLifter(value: Float)

    fun sendGrabber(value: GrabberInstruction)

    fun updateRobotStatus()

    fun endConnections()

    fun sendMapRequest()

    fun listenForMap()

}