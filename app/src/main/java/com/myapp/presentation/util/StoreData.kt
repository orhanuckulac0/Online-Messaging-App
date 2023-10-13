package com.myapp.presentation.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class StoreData(private val context: Context) {
    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("imageStored")
        private val Context.fcmDataStore: DataStore<Preferences> by preferencesDataStore("fmcTokenStored")
    }

    suspend fun storeImage(uri: String) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey("image")] = uri
        }

    }

    fun getImage(): Flow<String?> {
        return context.dataStore.data.map { preferences ->
            preferences[stringPreferencesKey("image")]
        }
    }

    suspend fun storeFCMToken(fcmToken: String){
        context.fcmDataStore.edit { preferences->
            preferences[stringPreferencesKey("fcm_token")] = fcmToken
        }
    }

    fun getFCMToken(): Flow<String?> {
        return context.fcmDataStore.data.map { preferences->
            preferences[stringPreferencesKey("fcm_token")]
        }
    }
}
