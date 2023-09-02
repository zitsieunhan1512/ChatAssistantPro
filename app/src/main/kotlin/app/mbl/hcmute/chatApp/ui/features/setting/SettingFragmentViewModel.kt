package app.mbl.hcmute.chatApp.ui.features.setting

import android.view.View
import androidx.lifecycle.viewModelScope
import app.mbl.hcmute.base.common.BaseViewModel
import app.mbl.hcmute.chatApp.R
import app.mbl.hcmute.chatApp.base.preference.AppSettings.MODE_NIGHT_DEFAULT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingFragmentViewModel @Inject constructor(private val dataStore: app.mbl.hcmute.chatApp.data.local.datastore.DataStoreManager) : BaseViewModel() {
    val themeValue = MutableStateFlow(MODE_NIGHT_DEFAULT.mapNightModeToSeekBar())

    init {
        initThemeMode()
    }

    private fun initThemeMode() {
        viewModelScope.launch {
            dataStore.getSettingStream(app.mbl.hcmute.chatApp.base.preference.AppSettings.NIGHT_MODE)
                .map { it?.mapNightModeToSeekBar() ?: MODE_NIGHT_DEFAULT.mapNightModeToSeekBar() }
                .collect { progress -> themeValue.tryEmit(progress) }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
        }
    }

    private fun createStarDot(length: Int): String {
        var result = ""
        for (i in 0 until length) result += "*"
        return result
    }
}