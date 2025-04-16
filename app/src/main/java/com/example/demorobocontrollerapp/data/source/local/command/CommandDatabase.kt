package com.example.demorobocontrollerapp.data.source.local.command

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [LocalCommand::class], version = 1, exportSchema = false)
abstract class CommandDatabase : RoomDatabase() {
    abstract fun commandDao(): CommandDao
}