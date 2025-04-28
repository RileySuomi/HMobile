package com.example.demorobocontrollerapp.data

import com.example.demorobocontrollerapp.data.source.local.command.CommandDao
import com.example.demorobocontrollerapp.data.source.local.command.LocalCommand
import com.example.demorobocontrollerapp.data.source.local.settings.DataStoreRepo
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.GrabberInstruction
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.NetworkGrabberInstruction
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.NetworkMovementInstruction
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.NetworkResultDataSource
import com.example.demorobocontrollerapp.scoping.ApplicationScope
import com.example.demorobocontrollerapp.scoping.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class DefaultRobotInfoRepository @Inject constructor(
    private val networkResultDataSource: NetworkResultDataSource,
    private val localCommandDataSource: CommandDao,
    private val settingsDataSource: DataStoreRepo,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher,
    @ApplicationScope private val scope: CoroutineScope,
) : RobotInfoRepository {
    private var commandIndex = 0

    override suspend fun updateSettings(key: String, value: String): Int {
        settingsDataSource.putString(key, value)
        return 1
    }

    override suspend fun getSetting(key: String): Flow<String> {
        return settingsDataSource.getString(key)
    }

    override suspend fun sendMovement(speed: Float, angular: Float) {
        networkResultDataSource.sendMovement(speed, angular)
        localCommandDataSource.upsert(LocalCommand(commandIndex, com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.NetworkMovementInstruction(
            speed,
            angular
        ).toString()))
        commandIndex += 1
    }

    override suspend fun sendGrabber(height: Int, instruction: com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.GrabberInstruction) {
        networkResultDataSource.sendGrabber(height, instruction)
        localCommandDataSource.upsert(LocalCommand(commandIndex, com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.NetworkGrabberInstruction(
            height,
            instruction
        ).toString()))
        commandIndex += 1
    }

    override fun getUpdates(): Flow<List<Command>> {
        TODO("Not yet implemented")
    }


}