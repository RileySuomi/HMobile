package com.example.demorobocontrollerapp.data.source.network.tcpdatarequests

interface NetworkResultDataSource {
    enum class GrabberStatus

    fun startConnection(host: String, port: Int)

    fun sendMovement(speed: Float, angular: Float)

    fun sendGrabber(value: Int, grabber: GrabberInstruction)

    fun updateRobotStatus()

    fun endConnections()

}