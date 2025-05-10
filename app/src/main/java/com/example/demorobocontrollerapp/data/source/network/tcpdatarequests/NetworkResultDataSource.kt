package com.example.demorobocontrollerapp.data.source.network.tcpdatarequests

interface NetworkResultDataSource {
    enum class GrabberStatus

    fun startConnection(host: String, port: Int)

    fun sendMovement(speed: Float, angular: Float)

    fun sendLifter(value: Float)

    fun sendGrabber(value: GrabberInstruction)

    fun updateRobotStatus()

    fun endConnections()

}