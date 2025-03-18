package com.example.data_store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATA_STORE_FILE_NAME = "auth"
val Context.dataStore by preferencesDataStore(name = DATA_STORE_FILE_NAME)

class AuthDataStoreRepository(private val context: Context) {
    suspend fun setUserRememberedFlag(value: Boolean) {
        context.dataStore.edit { settings ->
            settings[isUserRememberedKey] = value
        }
    }

    suspend fun setAccessToken(token: String) {
        context.dataStore.edit { settings ->
            settings[accessTokenKey] = token
        }
    }

    suspend fun setRefreshToken(token: String) {
        context.dataStore.edit { settings ->
            settings[refreshTokenKey] = token
        }
    }

    fun getUserRememberedFlag(): Flow<Boolean?> {
        return context.dataStore.data.map { settings ->
            settings[isUserRememberedKey]
        }
    }

    fun getAccessToken(): Flow<String?> {
        return context.dataStore.data.map { settings ->
            settings[accessTokenKey]
        }
    }

    fun getRefreshToken(): Flow<String?> {
        return context.dataStore.data.map { settings ->
            settings[refreshTokenKey]
        }
    }

    companion object DataStoreKeys {
        val isUserRememberedKey = booleanPreferencesKey("is_user_remembered")
        val accessTokenKey = stringPreferencesKey("access_token")
        val refreshTokenKey = stringPreferencesKey("refresh_token")
    }
}