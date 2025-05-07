package com.example.demorobocontrollerapp.data.source.local.settings.daoversion

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "settingsObjects"
)
data class LocalSetting (
    @PrimaryKey val settingName: String,
    val settingDisplayName: String,
    val settingValue: String,
    val editable: Boolean
)
