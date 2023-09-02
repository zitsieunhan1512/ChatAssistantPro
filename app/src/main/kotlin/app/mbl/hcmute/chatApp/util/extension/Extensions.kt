package app.mbl.hcmute.chatApp.util.extension

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import kotlin.math.abs

inline fun <T : ViewBinding> AppCompatActivity.viewInflateBinding(crossinline bindingInflater: (LayoutInflater) -> T) =
    lazy(LazyThreadSafetyMode.NONE) {
        bindingInflater.invoke(layoutInflater)
    }

fun Int.mapToNearestNumber(setOfNumbers: Set<Int>): Int {
    var nearestNumber = 0
    var smallestDifference = Int.MAX_VALUE

    for (number in setOfNumbers) {
        val difference = abs(this - number)
        if (difference < smallestDifference) {
            smallestDifference = difference
            nearestNumber = number
        }
    }
    return nearestNumber
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

fun Fragment.getNavigationResult(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>(key)

fun Fragment.setNavigationResult(result: Any, key: String = "result") {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, result)
}