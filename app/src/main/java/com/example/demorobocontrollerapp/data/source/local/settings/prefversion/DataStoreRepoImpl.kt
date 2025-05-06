package com.example.demorobocontrollerapp.data.source.local.settings.prefversion

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.demorobocontrollerapp.data.source.local.settings.prefversion.dataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreRepoImpl @Inject constructor(@ApplicationContext context: Context): DataStoreRepo {
    private val dataStore = context.dataStore

    override suspend fun putString(key: String, value: String) {
        dataStore.edit { preferences ->
            preferences[stringPreferencesKey(key)] = value
        }
    }

    override suspend fun putBoolean(key: String, value: Boolean) {
        dataStore.edit { preferences ->
            preferences[booleanPreferencesKey(key)] = value
        }
    }

    override fun getAllStrings(): Flow<Pair<String, String>> {
        return dataStore.data.map {
            Pair(it.asMap().keys.toString(), it.asMap().values.toString())
        }
    }

    override suspend fun getString(key: String): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[stringPreferencesKey(key)] ?: ""
        }
    }

    override suspend fun getBoolean(key: String): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[booleanPreferencesKey(key)] ?: false
        }
    }

    override suspend fun clearPReferences(key: String) {
        dataStore.edit { preferences ->
            preferences.remove(stringPreferencesKey(key))
        }
    }
}