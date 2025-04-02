package com.example.demorobocontrollerapp

import android.content.Context
import androidx.test.espresso.core.internal.deps.dagger.Provides
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDataStoreRepo(@ApplicationContext context: Context): DataStoreRepo {
        return DataStoreRepoImpl(context)
    }
}