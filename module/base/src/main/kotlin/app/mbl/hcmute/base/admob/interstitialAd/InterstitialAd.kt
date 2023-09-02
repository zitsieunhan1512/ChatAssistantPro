package app.mbl.hcmute.base.admob.interstitialAd

import android.app.Activity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import app.mbl.hcmute.base.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

interface IInterstitialAd : DefaultLifecycleObserver {
    val INTERSTITIAL_AD_UNIT_ID: String
        get() {
            Timber.tag("IInterstitialAd").d("INTERSTITIAL_AD_UNIT_ID: ${BuildConfig.INTERSTITIAL_AD_UNIT_ID}")
            return BuildConfig.INTERSTITIAL_AD_UNIT_ID
        }
    val isInterstitialAdLoading: MutableStateFlow<Boolean>
    fun configureInterstitialAd(activity: Activity, isAutomaticShowOneTime: Boolean = false, lifecycle: Lifecycle)
    fun showInterstitialAd(activity: Activity, onAdNotLoaded: () -> Unit)
}