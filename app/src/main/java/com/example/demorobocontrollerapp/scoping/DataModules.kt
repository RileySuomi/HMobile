package com.example.demorobocontrollerapp.scoping

import android.content.ContentValues
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.demorobocontrollerapp.data.Command
import com.example.demorobocontrollerapp.data.DefaultRobotInfoRepository
import com.example.demorobocontrollerapp.data.RobotInfoRepository
import com.example.demorobocontrollerapp.data.source.local.command.CommandDao
import com.example.demorobocontrollerapp.data.source.local.command.CommandDatabase
import com.example.demorobocontrollerapp.data.source.local.settings.daoversion.SettingsDao
import com.example.demorobocontrollerapp.data.source.local.settings.daoversion.SettingsDatabase
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.NetworkResultDataSource
import com.example.demorobocontrollerapp.data.source.network.tcpdatarequests.RobotNetworkDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.Executors
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
object DatabaseModule  {

    @Singleton
    @Provides
    fun provideCommandDatabase(@ApplicationContext context: Context): CommandDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            CommandDatabase::class.java,
            "Commands.db"

        ).build()
    }

    @Provides
    fun provideCommandsDao(database: CommandDatabase): CommandDao = database.commandDao()


    @Singleton
    @Provides
    fun provideSettingDatabase(@ApplicationContext context: Context): SettingsDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            SettingsDatabase::class.java,
            "Settings.db"
        ).createFromAsset("presetSettingsDatabase.db")
            .build()
    }

    @Provides
    fun provideSettingsDao(database: SettingsDatabase): SettingsDao = database.settingsDao()

}

