package com.example.demorobocontrollerapp.data

import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.GrabberInstruction
import kotlinx.coroutines.flow.Flow

interface RobotInfoRepository {

    suspend fun updateSettings(key: String, value: String): Int

    suspend fun sendMovement(speed: Float, angular: Float)

    suspend fun sendGrabber(height: Int, instruction: GrabberInstruction)

    fun getUpdates(): Flow<List<Command>>

    suspend fun getSetting(key: String): Flow<String>
}