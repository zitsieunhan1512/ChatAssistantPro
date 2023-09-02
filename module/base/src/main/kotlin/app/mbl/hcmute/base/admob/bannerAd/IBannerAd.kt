package app.mbl.hcmute.base.admob.bannerAd

import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle

interface IBannerAd : DefaultLifecycleObserver {
    fun addIBannerAdObserveLifecycle(lifecycle: Lifecycle)
    fun addBannerView(fragment: Fragment, adViewContainer: FrameLayout)
    fun removeIBannerAdObserveLifecycle(lifecycle: Lifecycle)
}