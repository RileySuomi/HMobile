package com.example.demorobocontrollerapp

import android.content.Context
import androidx.test.espresso.core.internal.deps.dagger.Provides
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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