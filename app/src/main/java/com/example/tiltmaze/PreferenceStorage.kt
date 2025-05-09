package com.example.tiltmaze

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

class PreferenceStorage(private val context: Context) {
    companion object {
        private val Context.dataStore by preferencesDataStore("app_prefs")

        private object PreferenceKeys {
            val BALL_COLOR = stringPreferencesKey("ballColor")
            val CONFIRM_SHOW_TIMER = booleanPreferencesKey("confirmShowTimer")
            val BALL_SPEED = intPreferencesKey("ballSpeed")
        }
    }

    val appPreferencesFlow = context.dataStore.data.map { preferences ->
        val ballColor = preferences[PreferenceKeys.BALL_COLOR] ?: BallColor.RED.name
        val confirmShowTimer = preferences[PreferenceKeys.CONFIRM_SHOW_TIMER] ?: true
        val ballSpeed = preferences[PreferenceKeys.BALL_SPEED] ?: 3

        AppPreferences(enumValueOf<BallColor>(ballColor), confirmShowTimer, ballSpeed)
    }

    suspend fun saveBallColor(color: BallColor) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.BALL_COLOR] = color.name
        }
    }
    suspend fun saveConfirmShowTimer(confirm: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.CONFIRM_SHOW_TIMER] = confirm
        }
    }

    suspend fun saveBallSpeed(ballSpeed: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferenceKeys.BALL_SPEED] = ballSpeed
        }
    }
}