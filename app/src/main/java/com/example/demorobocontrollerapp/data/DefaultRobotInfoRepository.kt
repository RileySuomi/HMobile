package com.example.demorobocontrollerapp.data

import android.graphics.Bitmap
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.demorobocontrollerapp.data.source.local.command.CommandDao
import com.example.demorobocontrollerapp.data.source.local.command.LocalCommand
import com.example.demorobocontrollerapp.data.source.local.settings.daoversion.LocalSetting
import com.example.demorobocontrollerapp.data.source.local.settings.daoversion.SettingsDao
import com.example.demorobocontrollerapp.data.source.local.settings.prefversion.DataStoreRepo
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.GrabberInstruction
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.NetworkGrabberInstruction
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.NetworkMovementInstruction
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.NetworkResultDataSource
import com.example.demorobocontrollerapp.scoping.ApplicationScope
import com.example.demorobocontrollerapp.scoping.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton
import android.util.Log

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
        networkResultDataSource.sendMovement(speed, angular)
        localCommandDataSource.upsert(LocalCommand(commandIndex, com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.NetworkMovementInstruction(
            speed,
            angular
        ).toString()))
        commandIndex += 1
    }

    override suspend fun sendGrabber(instruction: GrabberInstruction) {
        networkResultDataSource.sendGrabber(instruction)
        localCommandDataSource.upsert(LocalCommand(commandIndex, NetworkGrabberInstruction(
            instruction
        ).toString()))
        commandIndex += 1
    }

    override suspend fun sendLiftLower(height: Float) {
        networkResultDataSource.sendLifter(height)
    }

    override fun getUpdates(): Flow<List<Command>> {
        TODO("Not yet implemented")
    }

    override suspend fun sendMapRequest(){
        networkResultDataSource.sendMapRequest()
    }

    override suspend fun listenForMap(){
        networkResultDataSource.listenForMap()
    }

    override suspend fun getMapState():Flow<Bitmap>{
        return flow{
            networkResultDataSource.currentMap
        }
    }


}