package com.example.newssubscription.alarm.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.newssubscription.alarm.domain.model.Alarm
import com.google.gson.Gson
import kotlinx.coroutines.flow.first

class AlarmPreferences(private val context: Context) {
    private val gson by lazy { Gson() }

    companion object {
        private val ALARM_KEY = stringPreferencesKey("alarm_key")
    }

    suspend fun saveAlarm(alarm: Alarm) {
        val alarmJson = gson.toJson(alarm)
        context.dataStore.edit { it[ALARM_KEY] = alarmJson }
    }

    suspend fun readAlarm(): Alarm? {
        val preferences = context.dataStore.data.first()
        val alarmJson = preferences[ALARM_KEY]
        return alarmJson?.let { gson.fromJson(it, Alarm::class.java) }
    }

    suspend fun clearAlarm() {
        context.dataStore.edit { it.remove(ALARM_KEY) }
    }
}

// Extension for DataStore initialization
private const val ALARM_PREFERENCES = "alarm_preferences"
private val Context.dataStore by preferencesDataStore(name = ALARM_PREFERENCES)