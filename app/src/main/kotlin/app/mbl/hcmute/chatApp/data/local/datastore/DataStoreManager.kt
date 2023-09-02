package app.mbl.hcmute.chatApp.data.local.datastore

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LifecycleCoroutineScope
import app.mbl.hcmute.chatApp.base.preference.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore("settings")

@Singleton
class DataStoreManager @Inject constructor(@ApplicationContext appContext: Context) {

    private val settingsDataStore = appContext.dataStore

    suspend fun setThemeMode(mode: Int) = settingsDataStore.edit { settings -> settings[AppSettings.NIGHT_MODE] = mode }
    fun setThemeMode(lifecycle: LifecycleCoroutineScope, mode: Int) {
        lifecycle.launch {
            settingsDataStore.edit { settings -> settings[AppSettings.NIGHT_MODE] = mode }
        }
    }

    suspend fun <T> getSetting(key: Preferences.Key<T>) =
        settingsDataStore.data.map { it[key] }.firstOrNull().also { Timber.d("Get setting ${key.name} : $it") }

    fun <T> getSettingStream(key: Preferences.Key<T>) = settingsDataStore.data.map { preferences -> preferences[key] }

    suspend fun <T> setSetting(key: Preferences.Key<T>, value: T) {
        settingsDataStore.edit { settings -> settings[key] = value }.also { Timber.d("Update setting ${key.name} : $value") }
    }

    fun <T> setSetting(lifecycle: LifecycleCoroutineScope, key: Preferences.Key<T>, value: T) {
        lifecycle.launch {
            settingsDataStore.edit { settings -> settings[key] = value }.also { Timber.d("Update setting ${key.name} : $value") }
        }
    }

}