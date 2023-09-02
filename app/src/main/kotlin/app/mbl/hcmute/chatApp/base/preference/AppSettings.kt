package app.mbl.hcmute.chatApp.base.preference

import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.intPreferencesKey

object AppSettings {

    //SYSTEM_SETTING
//    val SAMPLE_STRING_KEY = stringPreferencesKey("SAMPLE_STRING")
//    val SAMPLE_BOOLEAN_KEY = booleanPreferencesKey("SAMPLE_BOOLEAN")

    //GLOBAL_SETTING
    val NIGHT_MODE = intPreferencesKey("night_mode")
    val MODE_NIGHT_DEFAULT = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM else AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY

}