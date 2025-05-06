package com.example.demorobocontrollerapp.data.source.local.settings.daoversion

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.demorobocontrollerapp.data.source.local.command.LocalCommand
import kotlinx.coroutines.flow.Flow

@Dao
interface SettingsDao {
    @Query("SELECT * FROM settingsObjects")
    fun observeSettings(): Flow<List<LocalSetting>>

    @Query("SELECT * FROM settingsObjects WHERE settingName = :settingName LIMIT 1")
    fun observeSetting(settingName: String): Flow<LocalSetting>

    @Upsert
    suspend fun upsert(setting: LocalSetting)

    @Query("DELETE FROM settingsObjects WHERE settingName = :settingName")
    suspend fun deletedById(settingName: String): Int

}