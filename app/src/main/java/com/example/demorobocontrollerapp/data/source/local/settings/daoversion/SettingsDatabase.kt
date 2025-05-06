package com.example.demorobocontrollerapp.data.source.local.settings.daoversion

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocalSetting::class], version = 1, exportSchema = false)
abstract class SettingsDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
}