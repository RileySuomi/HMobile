package com.example.demorobocontrollerapp.data

import com.example.demorobocontrollerapp.data.source.local.command.LocalCommand
import com.example.demorobocontrollerapp.data.source.local.settings.daoversion.LocalSetting


fun LocalSetting.toExternal() = Setting(
    settingName = settingName,
    settingDisplayName = settingDisplayName,
    settingValue = settingValue,
    editable = editable,
)

@JvmName("localToExternal")
fun List<LocalSetting>.toExternal() = map(LocalSetting::toExternal)