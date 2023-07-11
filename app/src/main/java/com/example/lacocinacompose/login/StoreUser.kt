package com.example.lacocinacompose.login

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class StoreUser(private val context: Context) {
    companion object {
        private val Context.dataStoreeEmail: DataStore<Preferences> by preferencesDataStore("userEmail")
        val USER_EMAIL = stringPreferencesKey("user_email")

        val USER_PASS = stringPreferencesKey("user_pass")
    }

    val getEmail: Flow<String?> = context.dataStoreeEmail.data
        .map { preferences ->
            preferences[USER_EMAIL] ?: ""
        }
    suspend fun saveEmail(name: String) {
        context.dataStoreeEmail.edit { preferences ->
            preferences[USER_EMAIL] = name
        }
    }
    suspend fun savePass(name: String) {
        context.dataStoreeEmail.edit { preferences ->
            preferences[USER_PASS] = name
        }
    }
}