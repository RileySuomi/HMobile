package com.example.demorobocontrollerapp.data.source.local.command

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.type.DateTime

@Entity(
    tableName = "sentCommands"
)
data class LocalCommand (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var type: Int,
    var val1: Float,
    var val2: Float = 0f,
    var timestamp: Long = System.currentTimeMillis(),
    var undone: Boolean = false
)