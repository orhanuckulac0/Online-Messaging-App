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
}
