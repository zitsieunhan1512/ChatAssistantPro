package app.mbl.hcmute.base.ext

import android.view.View
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import kotlinx.coroutines.*
import timber.log.Timber

fun View.gone() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

@BindingAdapter("active")
fun View.active(value: Boolean = true) {
    isEnabled = value
    isClickable = value
    alpha = if (value) 1f else 0.3f
}

infix fun View.onClick(func: () -> Unit) {
    this.setOnClickListener { func.invoke() }
}

@BindingAdapter("enableView")
fun View.enableView(value: Boolean = true) {
    isEnabled = value
    alpha = if (value) 1f else 0.4f
}

@BindingAdapter("layoutHeight")
fun View.setLayoutHeight(dimen: Float) {
    val newLayoutParams = layoutParams
    newLayoutParams.height = dimen.toInt()
    layoutParams = newLayoutParams
}

@BindingAdapter("layoutMarginTop")
fun View.setLayoutMarginTop(marginTop: Float) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val newParams = layoutParams as ViewGroup.MarginLayoutParams
        newParams.setMargins(newParams.marginStart, marginTop.toInt(), newParams.rightMargin, newParams.bottomMargin)
        layoutParams = newParams
    }
}

private const val DEBOUNCE_DELAY = 1000L // in milliseconds

fun View.setDebouncedOnClickListener(scope: CoroutineScope, delayTime: Long = DEBOUNCE_DELAY, onClickListener: () -> Unit) {
    Timber.tag("setDebouncedOnClickListener").d("is scope active: ${scope.isActive}")
    setOnClickListener {
        scope.launch(Dispatchers.Main) {
            isEnabled = false // disable the view
            Timber.tag("setDebouncedOnClickListener").d("start DEBOUNCE_DELAY")
            onClickListener.invoke() // call the original onClickListener
            delay(delayTime) // wait for the debounce delay
            Timber.tag("setDebouncedOnClickListener").d("after DEBOUNCE_DELAY")
            isEnabled = true // re-enable the view
        }
    }
}