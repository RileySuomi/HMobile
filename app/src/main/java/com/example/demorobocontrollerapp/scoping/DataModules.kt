package com.example.demorobocontrollerapp.scoping

import android.content.Context
import androidx.room.Room
import com.example.demorobocontrollerapp.data.DefaultRobotInfoRepository
import com.example.demorobocontrollerapp.data.RobotInfoRepository
import com.example.demorobocontrollerapp.data.source.local.command.CommandDao
import com.example.demorobocontrollerapp.data.source.local.command.CommandDatabase
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.NetworkResultDataSource
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.RobotNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindTaskRepository(repository: DefaultRobotInfoRepository): RobotInfoRepository
}

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Singleton
    @Binds
    abstract fun bindNetworkDataSource(dataSource: RobotNetworkDataSource): NetworkResultDataSource
}


@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): CommandDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CommandDatabase::class.java,
            "Tasks.db"
        ).build()
    }

    @Provides
    fun provideCommandDao(database: CommandDatabase): CommandDao = database.commandDao()
}