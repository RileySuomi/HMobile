package com.example.demorobocontrollerapp.data.source.network.tcpdatarequests

import android.graphics.Bitmap
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.MapMetadata

interface NetworkResultDataSource {
    enum class GrabberStatus
    var currentMap: Bitmap
    var mapMetadata: MapMetadata?

    fun startConnection(host: String, port: Int)

    fun sendMovement(speed: Float, angular: Float)

    fun sendLifter(value: Float)

    fun sendExtend(value: Float)

    fun sendGrabber(value: GrabberInstruction)

    fun updateRobotStatus()

    fun endConnections()

    fun sendMapRequest()

    fun listenForMap(onMapReceived: (Bitmap) -> Unit)

    fun sendZeroLift(value: Float)

    fun sendZeroRetract(value: Float)

    fun sendCoordinates(xCoordinate: Float, yCoordinate: Float)

    fun sendAndGetMap(): Bitmap

}