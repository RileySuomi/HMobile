package com.example.demorobocontrollerapp

import com.example.demorobocontrollerapp.data.source.local.datastore.DataStoreRepo
import com.example.demorobocontrollerapp.data.source.local.datastore.DataStoreRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindDataStoreRepo(
        impl: DataStoreRepoImpl
    ): DataStoreRepo
}