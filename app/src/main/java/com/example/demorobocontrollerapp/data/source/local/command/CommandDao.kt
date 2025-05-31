package com.example.demorobocontrollerapp.data.source.local.command

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface CommandDao {
    @Query("SELECT * FROM sentCommands")
    fun observeCommands(): Flow<List<LocalCommand>>

    @Upsert
    suspend fun upsert(command: LocalCommand)

    @Query("SELECT id, type, val1, val2, timestamp, undone FROM sentCommands WHERE timestamp = (SELECT MAX(timestamp) from sentCommands WHERE undone IS 0 ORDER BY timestamp DESC LIMIT 1)")
    suspend fun getForUndo(): LocalCommand?

    @Query("SELECT id, type, val1, val2, timestamp, undone FROM sentCommands WHERE timestamp = (SELECT MAX(timestamp) from sentCommands WHERE undone IS 1 ORDER BY timestamp DESC LIMIT 1)")
    suspend fun getForRedo(): LocalCommand?

    @Query("UPDATE sentCommands SET undone = 1 WHERE timestamp = (SELECT MAX(timestamp) from sentCommands WHERE undone IS 0 ORDER BY timestamp DESC LIMIT 1)")
    suspend fun markUndo()

    @Query("UPDATE sentCommands SET undone = 0 WHERE id = (SELECT id from sentCommands WHERE undone IS 1 ORDER BY timestamp ASC LIMIT 1 )")
    suspend fun markRedo()

    @Query("DELETE FROM sentCommands WHERE timestamp NOT IN (SELECT timestamp from sentCommands ORDER BY timestamp DESC LIMIT 30)")
    suspend fun deleteOverHistory()

    @Query("DELETE FROM sentCommands")
    suspend fun deleteCache()

    @Transaction
    suspend fun getAndMarkRedo(): LocalCommand? {
        val cmd = getForRedo()
        markRedo()
        return cmd;

    }

    @Transaction
    suspend fun getAndMarkUndo(): LocalCommand? {
        val cmd = getForUndo()
        markUndo()
        return cmd;
    }

    @Transaction
    suspend fun updateAndRemove(command: LocalCommand) {
        upsert(command)
        deleteOverHistory()
    }


    @Query("DELETE FROM sentCommands WHERE id = :commandId")
    suspend fun deletedById(commandId: Int): Int

    @Query("SELECT * FROM sentCommands WHERE id IN (SELECT id from sentCommands limit 1)")
    suspend fun getTop(): LocalCommand

    @Query("DELETE FROM sentCommands WHERE id IN (SELECT id from sentCommands limit 1)")
    suspend fun deleteTop(): Int
}