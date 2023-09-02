package app.mbl.hcmute.chatApp.ui.features.setting

import android.content.Context
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate

enum class SettingConst(val settingValue: String) {
    ABOUT_URL("https://google.com/"),
    POLICY_URL("https://google.com/"),
    TERM_URL("https://google.com/")
}

fun Int.mapNightModeToSeekBar() = when (this) {
    AppCompatDelegate.MODE_NIGHT_YES -> 0
    AppCompatDelegate.MODE_NIGHT_NO -> 1
    else -> 2
}

fun Int.mapSeekbarToNightMode() = when (this) {
    0 -> AppCompatDelegate.MODE_NIGHT_YES
    1 -> AppCompatDelegate.MODE_NIGHT_NO
    else -> app.mbl.hcmute.chatApp.base.preference.AppSettings.MODE_NIGHT_DEFAULT
}

fun Context.showKeyboard(view: View) {
    this.getSystemService(Context.INPUT_METHOD_SERVICE)?.let { imm ->
        (imm as InputMethodManager).showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
    }
}

fun EditText.onDone(callback: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            callback.invoke()
            return@setOnEditorActionListener true
        }
        false
    }
}