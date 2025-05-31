package com.example.demorobocontrollerapp.data

import android.graphics.Bitmap
import com.example.demorobocontrollerapp.data.source.local.command.CommandDao
import com.example.demorobocontrollerapp.data.source.local.command.LocalCommand
import com.example.demorobocontrollerapp.data.source.local.settings.daoversion.LocalSetting
import com.example.demorobocontrollerapp.data.source.local.settings.daoversion.SettingsDao
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.NetworkResultDataSource
import com.example.demorobocontrollerapp.scoping.ApplicationScope
import com.example.demorobocontrollerapp.scoping.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log
import com.example.demorobocontrollerapp.data.GrabberInstruction
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.MapMetadata

@Singleton
class DefaultRobotInfoRepository @Inject constructor(
    private val networkResultDataSource: NetworkResultDataSource,
    private val localCommandDataSource: CommandDao,
    private val settingsDataSource: SettingsDao,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : RobotInfoRepository {
    private var commandIndex = 0

    override suspend fun updateSettings(key: String, screenName: String, value: String, editable: Boolean): Int {
        settingsDataSource.upsert(LocalSetting(key, screenName, value, editable))
        return 1
    }

    override suspend fun getSetting(key: String): Flow<Setting> {
        return flow {
            settingsDataSource.observeSetting(key).map { item -> item.toExternal() }
        }
    }

    override suspend fun updateSetting(key: String, value: String) {
        settingsDataSource.updateSettingValue(key, value)
    }

    override fun getSettings(): Flow<List<Setting>> {
        return settingsDataSource.observeSettings().map { item -> item.toExternal() }
    }

    override fun clearSettings() {
        return settingsDataSource.clearTable()
    }



    override fun beginCommunication() {
        var host: String = ""
        host = settingsDataSource.observeSingleSetting("robotIp").settingValue
        Log.d("NetworkData","Getting Port")
        val port: Int = settingsDataSource.observeSingleSetting("robotPort").settingValue.toInt()
        Log.d("NetworkData","Host is: ${host}")
        networkResultDataSource.startConnection(host, port)
    }

    override fun endCommunication() {
        networkResultDataSource.endConnections()
    }

    override suspend fun sendMovement(speed: Float, angular: Float) {
        localCommandDataSource.updateAndRemove(LocalCommand(type = CommandType.SendMove.value, val1 = speed, val2 = angular))
        networkResultDataSource.sendMovement(speed, angular)
    }

    override suspend fun sendGrabber(instruction: com.example.demorobocontrollerapp.data.GrabberInstruction) {
        localCommandDataSource.updateAndRemove(LocalCommand(type = CommandType.SendGrab.value, val1 = instruction.value.toFloat()))
        networkResultDataSource.sendGrabber(instruction)
    }

    override suspend fun dropUndoCache() {
        localCommandDataSource.deleteCache()
    }

    override suspend fun undo(): Boolean {
        val command = localCommandDataSource.getAndMarkUndo()
        command?.let {
            val c = LocalToData(it)

            when (c.commandType) {
                CommandType.SendMove -> {
                    networkResultDataSource.sendMovement(-c.speed, c.angularSpeed)
                    return true
                }
                else -> {
                    return false
                }
            }
        } ?: run {
            return false;
        }
    }

    override suspend fun redo(): Boolean {
        val command = localCommandDataSource.getAndMarkRedo()
        command?.let {
            val c = LocalToData(it)

            when (c.commandType) {
                CommandType.SendMove -> {
                    networkResultDataSource.sendMovement(c.speed, c.angularSpeed)
                    return true
                }
                else -> {
                    return false
                }
            }
        } ?: run {
            return false
        }
    }

    override suspend fun sendLiftLower(height: Float) {
        networkResultDataSource.sendLifter(height)
    }

    override suspend fun sendExtendRetract(height: Float) {
        networkResultDataSource.sendExtend(height)
    }

    override fun getUpdates(): Flow<List<Command>> {
        TODO("Not yet implemented")
    }

    override suspend fun giveMeMap(): Bitmap {
        return networkResultDataSource.sendAndGetMap()
    }

    override suspend fun sendMapRequest(){
        networkResultDataSource.sendMapRequest()
    }

    override suspend fun listenForMap(onMapReceived: (Bitmap) -> Unit){
        networkResultDataSource.listenForMap(onMapReceived)
    }

    override suspend fun sendCoordinates(xCoordinate: Float, yCoordinate: Float){
        networkResultDataSource.sendCoordinates(xCoordinate, yCoordinate)
    }

    override suspend fun getMapMetadata(): MapMetadata? {
        return networkResultDataSource.mapMetadata
    }

    override suspend fun getMapState():Flow<Bitmap>{
        return flow{
            emit(networkResultDataSource.currentMap)
        }
    }

    override suspend fun sendZeroMovementLift(height: Float) {
        networkResultDataSource.sendZeroLift(height)
    }

    override suspend fun sendZeroMovementRetract(height: Float) {
        networkResultDataSource.sendZeroRetract(height)
    }


}