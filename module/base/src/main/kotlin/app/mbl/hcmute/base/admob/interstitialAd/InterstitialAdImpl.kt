package app.mbl.hcmute.base.admob.interstitialAd

import android.app.Activity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

class InterstitialAdImpl : IInterstitialAd {
    private var interstitialAd: InterstitialAd? = null
    private var isAutomaticShow = false
    private lateinit var activity: Activity
    override val isInterstitialAdLoading = MutableStateFlow(false)

    override fun configureInterstitialAd(activity: Activity, isAutomaticShowOneTime: Boolean, lifecycle: Lifecycle) {
        Timber.d(message = "configureInterstitialAd isPreventShowingAdsWhenChangingConfigurations = $isPreventShowingAdsWhenChangingConfigurations")

        // always update
        lifecycle.addObserver(this)
        this.activity = activity

        if (isPreventShowingAdsWhenChangingConfigurations) return

        this.isAutomaticShow = isAutomaticShowOneTime
        loadInterstitialAd(activity = activity)
        isPreventShowingAdsWhenChangingConfigurations = false // reset value to default
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        isPreventShowingAdsWhenChangingConfigurations = activity.isChangingConfigurations
        Timber.d(message = "isPreventShowingAdsWhenChangingConfigurations = $isPreventShowingAdsWhenChangingConfigurations")
    }

    private fun loadInterstitialAd(activity: Activity) {
        Timber.d(message = "loadInterstitialAd")
        if (interstitialAd != null || isInterstitialAdLoading.value) return

        val adRequest = AdRequest.Builder().build()
        updateAdIsLoading(true)

        InterstitialAd.load(
            activity,
            INTERSTITIAL_AD_UNIT_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    updateAdIsLoading(false)
                    val error =
                        "domain: ${adError.domain}, code: ${adError.code}, " + "message: ${adError.message}"
                    Timber.e(message = error)
                }

                override fun onAdLoaded(ad: InterstitialAd) {
                    interstitialAd = ad
                    updateAdIsLoading(false)
                    Timber.d(message = "Ad was loaded.")
                    if (isAutomaticShow) {
                        showInterstitialAd(activity = activity, onAdNotLoaded = {
                            Timber.d(message = "Ad wasn't loaded.")
                        })
                        isAutomaticShow = false
                    }
                }
            }
        )
    }

    private fun updateAdIsLoading(isLoading: Boolean) {
        isInterstitialAdLoading.update { isLoading }
    }

    override fun showInterstitialAd(activity: Activity, onAdNotLoaded: () -> Unit) {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Timber.d("Ad was dismissed.")
                        interstitialAd = null
                        loadInterstitialAd(activity = activity)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Timber.d("Ad failed to show.")
                        interstitialAd = null
                    }

                    override fun onAdShowedFullScreenContent() {
                        Timber.d("Ad showed fullscreen content.")
                    }
                }
            interstitialAd?.show(activity)
        } else {
            Timber.d("Ad wasn't loaded.")
            onAdNotLoaded()
        }
    }

    companion object {
        private var isPreventShowingAdsWhenChangingConfigurations = false
    }
}