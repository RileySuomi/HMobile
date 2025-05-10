package com.example.demorobocontrollerapp.data

import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.GrabberInstruction
import kotlinx.coroutines.flow.Flow

interface RobotInfoRepository {

    fun beginCommunication()

    fun endCommunication()

    suspend fun updateSettings(key: String, screenName: String, value: String, editable: Boolean = true): Int

    suspend fun updateSetting(key: String, value: String)

    suspend fun sendMovement(speed: Float, angular: Float)

    suspend fun sendGrabber(instruction: GrabberInstruction)

    suspend fun sendLiftLower(height: Float)

    fun getUpdates(): Flow<List<Command>>

    suspend fun getSetting(key: String): Flow<Setting>

    fun getSettings(): Flow<List<Setting>>

    fun clearSettings()
}