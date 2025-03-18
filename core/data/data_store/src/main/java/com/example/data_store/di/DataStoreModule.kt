package com.example.data_store.di

import android.app.Application
import com.example.data_store.AuthDataStoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideDataStoreRepository(application: Application): AuthDataStoreRepository {
        return AuthDataStoreRepository(application.applicationContext)
    }
}