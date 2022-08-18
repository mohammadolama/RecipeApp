package com.android.example.recipeapp.datastore

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.android.example.recipeapp.presentation.BaseApplication
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsDatastore
@Inject
constructor(
    app: BaseApplication,
) {

    val context = app
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "settings"
    )

    private val scope = CoroutineScope(Main)

    val isDark = mutableStateOf(false)

    init {
        observeDatastore()

    }

    fun toggleTheme() {
        scope.launch {
            context.dataStore.edit { preferences ->
                val current = preferences[DARK_THEME_KEY] ?: false
                preferences[DARK_THEME_KEY] = !current
            }
        }
    }

    private fun observeDatastore() {
        context.dataStore.data.onEach { preferences ->
            preferences[DARK_THEME_KEY]?.let { isDarkTheme ->
                isDark.value = isDarkTheme
            }
        }.launchIn(scope = scope)
    }

    companion object {
        private val DARK_THEME_KEY = booleanPreferencesKey("dark_theme_key")
    }

}
