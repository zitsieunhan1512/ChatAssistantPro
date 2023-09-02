package app.mbl.hcmute.base.admob.bannerAd

import android.annotation.SuppressLint
import android.util.DisplayMetrics
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import app.mbl.hcmute.base.BuildConfig
import com.google.android.gms.ads.*
import timber.log.Timber

class IBannerAdImpl : IBannerAd {
    private val TAG = IBannerAdImpl::class.java.simpleName
    private var bannerAdView: AdView? = null
    private var initialLayoutComplete = false

    override fun addIBannerAdObserveLifecycle(lifecycle: Lifecycle) {
        Timber.d("addIBannerAdObserveLifecycle")
        lifecycle.addObserver(this)
    }

    override fun removeIBannerAdObserveLifecycle(lifecycle: Lifecycle) {
        Timber.d("removeIBannerAdObserveLifecycle")
        lifecycle.removeObserver(this)
    }

    override fun addBannerView(fragment: Fragment, adViewContainer: FrameLayout) {
        Timber.d("addBannerView")
        initialLayoutComplete = false
        bannerAdView = AdView(fragment.requireContext())
        bannerAdView?.adListener = object : AdListener() {
            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                Timber.d("onAdFailedToLoad: ${adError.message}")
            }

            override fun onAdImpression() {
                // Code to be executed when an impression is recorded
                // for an ad.
            }

            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }
        }
        adViewContainer.addView(bannerAdView)
        adViewContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                val adSize = calculateAdSize(fragment, adViewContainer)
                loadBanner(adSize)
            }
        }
    }

    override fun onResume(owner: LifecycleOwner) {
        Timber.d("onResume")
        bannerAdView?.resume()
        super.onResume(owner)
    }

    override fun onPause(owner: LifecycleOwner) {
        bannerAdView?.pause()
        Timber.d("onPause")
        super.onPause(owner)
    }


    override fun onDestroy(owner: LifecycleOwner) {
        bannerAdView?.destroy()
        Timber.d("onDestroy")
        super.onDestroy(owner)
    }

    @SuppressLint("MissingPermission")
    private fun loadBanner(adSize: AdSize) {
        bannerAdView?.adUnitId = BACKFILL_AD_UNIT_ID
        bannerAdView?.setAdSize(adSize)
        val adRequest = AdRequest.Builder().build()
        bannerAdView?.loadAd(adRequest)
        Timber.d("loadBanner")
    }

    private fun calculateAdSize(fragment: Fragment, adViewContainer: FrameLayout): AdSize {
        val activity = fragment.requireActivity()
        val outMetrics = DisplayMetrics()

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            val display = activity.display
            display?.getRealMetrics(outMetrics)
        } else {
            @Suppress("DEPRECATION")
            val display = activity.windowManager.defaultDisplay
            @Suppress("DEPRECATION")
            display.getMetrics(outMetrics)
        }

        val density = outMetrics.density

        var adWidthPixels = adViewContainer.width.toFloat()
        if (adWidthPixels == 0f) {
            adWidthPixels = outMetrics.widthPixels.toFloat()
        }

        val adWidth = (adWidthPixels / density).toInt()
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(fragment.requireContext(), adWidth)
    }

    companion object {
        internal val BACKFILL_AD_UNIT_ID: String
            get() {
                Timber.tag("IBannerAdImpl").d("REWARDED_AD_UNIT_ID: ${BuildConfig.REWARDED_AD_UNIT_ID}")
                return BuildConfig.BANNER_AD_UNIT_ID
            }
    }
}