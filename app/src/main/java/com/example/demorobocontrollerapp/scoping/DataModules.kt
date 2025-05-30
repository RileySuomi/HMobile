package com.example.demorobocontrollerapp.scoping

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.demorobocontrollerapp.data.Command
import com.example.demorobocontrollerapp.data.DefaultRobotInfoRepository
import com.example.demorobocontrollerapp.data.RobotInfoRepository
import com.example.demorobocontrollerapp.data.source.local.command.CommandDao
import com.example.demorobocontrollerapp.data.source.local.command.CommandDatabase
import com.example.demorobocontrollerapp.data.source.local.settings.daoversion.LocalSetting
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext

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
        val data = Room.databaseBuilder(
            context.applicationContext,
            SettingsDatabase::class.java,
            "Settings.db"
        ).addCallback(object: RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                db.execSQL("INSERT INTO settingsObjects (settingName, settingDisplayName, settingValue, editable) VALUES" +
                        "('robotIp', 'Robot IP', '72.233.179.204', 1)," +
                        "('robotPort', 'Robot Port', '65432', 1)," +
                        "('historyLength', 'History length', '20', 1);")
            }
        }).build()

//            .also { instance -> {
//            val scope = CoroutineScope(context = Dispatchers.Main)
//            scope.launch {
//                instance.settingsDao().clearTable()
//                instance.settingsDao().upsert(LocalSetting("robotIp", "Robot IP", "72.233.179.204"))
//                instance.settingsDao().upsert(LocalSetting("robotPort", "Robot port", "65432"))
//            }
//        } }
        return data
    }

    @Provides
    fun provideSettingsDao(database: SettingsDatabase): SettingsDao = database.settingsDao()

    private fun populateSettings() {

    }
}

