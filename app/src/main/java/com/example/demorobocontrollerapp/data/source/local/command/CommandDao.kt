package com.example.demorobocontrollerapp.data.source.local.command

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CommandDao {
    @Query("SELECT * FROM sentCommands")
    fun observeCommands(): Flow<List<LocalCommand>>

    @Upsert
    suspend fun upsert(command: LocalCommand)

    @Query("DELETE FROM sentCommands WHERE id = :commandId")
    suspend fun deletedById(commandId: Int): Int

    @Query("SELECT * FROM sentCommands WHERE id IN (SELECT id from sentCommands limit 1)")
    suspend fun getTop(): LocalCommand

    @Query("DELETE FROM sentCommands WHERE id IN (SELECT id from sentCommands limit 1)")
    suspend fun deleteTop(): Int
}