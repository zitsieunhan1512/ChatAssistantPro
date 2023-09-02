package app.mbl.hcmute.base.utils

import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import timber.log.Timber

class ViewUtils {
    companion object {
        private const val TABLET_MIN_DP = 589

        fun isTabletLayout(): Boolean {
            with(Resources.getSystem().displayMetrics) {
                return widthDP() >= TABLET_MIN_DP
            }
        }

        fun DisplayMetrics.widthDP() = (widthPixels.toFloat() / density).also {
            Timber.d("screenWidthDP", it.toString())
        }

        fun DisplayMetrics.heightDP() = (heightPixels.toFloat() / density).also {
            Timber.d("screenHeightDP", it.toString())
        }

        fun getPageWidthRatio(): Float {
            Resources.getSystem().displayMetrics.apply {
                return when (widthDP().toInt()) {
                    in 0..479 -> 1f
                    in 480..588 -> 1f
                    in 589..959 -> if (heightDP() > 411) 0.9f else 1f
                    in 960..1919 -> 0.75f
                    else -> 0.5f
                }.also { Timber.d("getPageWidthRatio", it.toString()) }
            }
        }

        fun getDialogWidthRatio(): Float {
            return when (Resources.getSystem().displayMetrics.widthDP().toInt()) {
                in 0..479 -> 1f
                in 480..672 -> 0.63f
                in 673..985 -> 0.55f
                in 986..1919 -> 0.35f
                else -> 0.25f
            }.also { Timber.d("getDialogWidthRatio", it.toString()) }
        }

        fun getDynamicViewWidth(): Int {
            return (getPageWidthRatio() * Resources.getSystem().displayMetrics.widthDP()).toInt().also {
                Timber.d("getDynamicViewWidth", it.toString())
            }
        }

        fun getOrientation(): Int {
            return Resources.getSystem().configuration.orientation.also {
                Timber.d("getOrientation", it.toString())
            }
        }

        fun isLandscapeOrientation(): Boolean {
            return getOrientation() == Configuration.ORIENTATION_LANDSCAPE
        }

        fun ConstraintLayout.updateGuidelinePercent(startGuidelineID: Int? = null, endGuidelineId: Int? = null) {
            if (startGuidelineID == null || endGuidelineId == null) return
            val startGuidePercent = (1 - getPageWidthRatio()) / 2
            val endGuidePercent = 1 - startGuidePercent
            Timber.d("updateGuidelinePercent", "start: $startGuidePercent, end: $endGuidePercent")
            ConstraintSet().builder(this)
                .buildGuideline(startGuidelineID, startGuidePercent)
                .buildGuideline(endGuidelineId, endGuidePercent)
                .applyTo(this)
        }

        fun getDynamicViewPadding() = getPaddingUsePercent(getPageWidthRatio())

        fun getPaddingUsePercent(percent: Float): Int {
            return ((Resources.getSystem().displayMetrics.widthDP() * (1 - percent)) / 2f).dpToPx().also {
                Timber.d("getPaddingUsePercent", "Screen width px: ${Resources.getSystem().displayMetrics.widthPixels}")
                Timber.d("getPaddingUsePercent", it.toString())
            }
        }

        fun getDynamicDialogWidth(): Int {
            return (getDialogWidthRatio() * Resources.getSystem().displayMetrics.widthPixels).toInt().also {
                Timber.d("getDynamicDialogWidth", it.toString())
            }
        }

        fun getWidthDP() = (Resources.getSystem().displayMetrics.widthPixels.toFloat() / Resources.getSystem().displayMetrics.density).toInt().also {
            Timber.d("getWidthDP", it.toString())
        }

        fun getHeightDP() = (Resources.getSystem().displayMetrics.heightPixels.toFloat() / Resources.getSystem().displayMetrics.density).toInt().also {
            Timber.d("screenHeightDP", it.toString())
        }

        fun Number.dpToPx(): Int {
            return (this.toFloat() * Resources.getSystem().displayMetrics.density).toInt()
        }

        fun Number.pxToDp(): Float {
            return this.toFloat() / Resources.getSystem().displayMetrics.density
        }

        fun ConstraintSet.builder(layout: ConstraintLayout): ConstraintSet {
            clone(layout)
            return this
        }

        fun ConstraintSet.buildGuideline(guideline: Int, percent: Float): ConstraintSet {
            setGuidelinePercent(guideline, percent)
            return this
        }

        fun View.disable() {
            alpha = 0.3f
            isEnabled = false
        }

        fun <T> LiveData<T>.observeUntilTrue(owner: LifecycleOwner, observer: (T) -> Unit) {
            observe(owner, object : Observer<T> {
                override fun onChanged(value: T) {
                    if (value is Boolean) {
                        if (value) {
                            removeObserver(this)
                            observer(value)
                        }
                    }
                }
            })
        }

        fun <T> LiveData<T>.observeOnce(owner: LifecycleOwner, observer: (T) -> Unit) {
            observe(owner, object : Observer<T> {
                override fun onChanged(value: T) {
                    if (value != null) {
                        removeObserver(this)
                        observer(value)
                    }
                }
            })
        }

        fun Int.toBoolean(): Boolean? = when (this) {
            1 -> true
            0 -> false
            else -> null
        }

        fun Boolean?.toInt(): Int = when (this) {
            true -> 1
            false -> 0
            null -> -1
        }
    }
}