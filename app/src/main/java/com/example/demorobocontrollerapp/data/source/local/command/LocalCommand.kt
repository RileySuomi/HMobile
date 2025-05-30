package com.example.demorobocontrollerapp.data.source.local.command

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime

@Entity(
    tableName = "sentCommands"
)
data class LocalCommand (
    @PrimaryKey(autoGenerate = true) val id: Int,
    var command: String,
    var timestamp: DateTime
)