package com.example.repartio.ui.theme

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

enum class DarkModePreference { SYSTEM, LIGHT, DARK }

@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val THEME_KEY = stringPreferencesKey("app_theme")
    private val DARK_MODE_KEY = stringPreferencesKey("dark_mode")

    val currentTheme: Flow<AppTheme> = context.dataStore.data.map { preferences ->
        val themeName = preferences[THEME_KEY] ?: AppTheme.OCEAN.name
        AppTheme.valueOf(themeName)
    }

    val darkModePreference: Flow<DarkModePreference> = context.dataStore.data.map { preferences ->
        val value = preferences[DARK_MODE_KEY] ?: DarkModePreference.SYSTEM.name
        DarkModePreference.valueOf(value)
    }

    suspend fun setTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

    suspend fun setDarkMode(preference: DarkModePreference) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = preference.name
        }
    }
}