package com.example.demorobocontrollerapp.data.source.local.command

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "sentCommands"
)
data class LocalCommand (
    @PrimaryKey val id: Int,
    var command: String,
)