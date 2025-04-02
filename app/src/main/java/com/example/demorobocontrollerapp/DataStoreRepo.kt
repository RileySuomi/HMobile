package com.example.demorobocontrollerapp

import kotlinx.coroutines.flow.Flow

interface DataStoreRepo {
    suspend fun putString(key:String,value:String)
    suspend fun putBoolean(key:String,value:Boolean)
    suspend fun getString(key: String): Flow<String>
    suspend fun getBoolean(key:String): Flow<Boolean>
    suspend fun clearPReferences(key: String)
}