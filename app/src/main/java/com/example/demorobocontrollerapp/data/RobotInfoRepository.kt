package com.example.demorobocontrollerapp.data

import android.graphics.Bitmap
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.GrabberInstruction
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.MapMetadata
import kotlinx.coroutines.flow.Flow

interface RobotInfoRepository {

    fun beginCommunication()

    fun endCommunication()

    suspend fun giveMeMap(): Bitmap

    suspend fun updateSettings(key: String, screenName: String, value: String, editable: Boolean = true): Int

    suspend fun updateSetting(key: String, value: String)

    suspend fun sendMovement(speed: Float, angular: Float)

    suspend fun sendGrabber(instruction: GrabberInstruction)

    suspend fun sendLiftLower(height: Float)

    suspend fun sendExtendRetract(height: Float)

    suspend fun sendMapRequest()

    suspend fun listenForMap(onMapReceived: (Bitmap) -> Unit)

    suspend fun sendZeroMovementLift(height: Float)

    suspend fun sendZeroMovementRetract(height: Float)

    suspend fun sendCoordinates(xCoordinate: Float, yCoordinate: Float)

    suspend fun getMapMetadata(): MapMetadata?

    suspend fun getMapState(): Flow<Bitmap>

    fun getUpdates(): Flow<List<Command>>

    suspend fun getSetting(key: String): Flow<Setting>

    fun getSettings(): Flow<List<Setting>>

    fun clearSettings()
}