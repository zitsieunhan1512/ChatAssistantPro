package app.mbl.hcmute.base.admob.openAppAd

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import app.mbl.hcmute.base.BuildConfig
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import java.util.*
import kotlin.coroutines.resume

class IOpenAppAdImpl : IOpenAppAd {
    private val appOpenAdManager = AppOpenAdManager()
    private var currentActivity: Activity? = null
    private val job = SupervisorJob()
    private var scope = CoroutineScope(job + Dispatchers.Main.immediate)
    private var isAutomaticShow = true
    override val isOpenAppAdLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    override fun configOpenAppAd(application: Application, lifecycle: Lifecycle) {
        registerActivityLifecycleCallbacks(application)
        registerLifecycleObserver(lifecycle)
        loadAd(application.applicationContext)
    }

    override fun onCreate(owner: LifecycleOwner) {
        Timber.tag(LOG_TAG).d("onCreate scope is active: ${scope.isActive}")
        super.onCreate(owner)
    }

    override fun onActivityCreated(p0: Activity, p1: Bundle?) {}

    private fun onMoveToForeground() {
        currentActivity?.let { appOpenAdManager.showAdIfAvailable(it) }
    }

    private fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

    private fun loadAd(context: Context) {
        Timber.tag(LOG_TAG).d("launch load open app entry")
        launchCoroutine {
            Timber.tag(LOG_TAG).d("launch load open app entry 2")
            try {
                withTimeout(5000) {
                    Timber.tag(LOG_TAG).d("launch load open app")
                    appOpenAdManager.loadAd(context)
                }
            } catch (e: Exception) {
                Timber.tag(LOG_TAG).d("load open app timeout")
                updateIsOpenAppAdLoading(false)
                if (e is TimeoutCancellationException) throw e
            }
        }
    }

    private fun registerActivityLifecycleCallbacks(application: Application) {
        application.registerActivityLifecycleCallbacks(this)
    }

    private fun registerLifecycleObserver(lifecycle: Lifecycle) {
        lifecycle.addObserver(this)
    }

    override fun onActivityStarted(activity: Activity) {
        Timber.tag(LOG_TAG).d("onActivityStarted: ${activity.componentName}, scope is active: ${scope.isActive}")
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(p0: Activity) {}

    override fun onActivityPaused(p0: Activity) {}

    override fun onActivityStopped(p0: Activity) {}

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

    override fun onActivityDestroyed(p0: Activity) {}

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        scope.cancel()
        Timber.tag(LOG_TAG).d("onDestroy, is scope active: ${scope.isActive}")
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        if (isPreventShowingAdsWhenChangingConfigurations) return
        onMoveToForeground()
        Timber.tag(LOG_TAG).d("showAds")
        Timber.tag(LOG_TAG).d("onStart")
        isPreventShowingAdsWhenChangingConfigurations = false
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        isPreventShowingAdsWhenChangingConfigurations = currentActivity?.isChangingConfigurations ?: false
    }

    private inner class AppOpenAdManager {

        private var appOpenAd: AppOpenAd? = null
        var isShowingAd = false

        private var loadTime: Long = 0

        suspend fun loadAd(context: Context): Boolean {

            if (isOpenAppAdLoading.value || isAdAvailable()) {
                return false
            }

            Timber.tag(LOG_TAG).d("loadAd entry")

            updateIsOpenAppAdLoading(true)

            val request = AdRequest.Builder().build()
            return suspendCancellableCoroutine { continuation ->
                AppOpenAd.load(
                    context,
                    AD_UNIT_ID,
                    request,
                    object : AppOpenAd.AppOpenAdLoadCallback() {

                        override fun onAdLoaded(ad: AppOpenAd) {
                            appOpenAd = ad
                            updateIsOpenAppAdLoading(false)
                            loadTime = Date().time
                            Timber.tag(LOG_TAG).d("onAdLoaded.")
                            if (isAutomaticShow) showAdIfAvailable(context)
                            isAutomaticShow = false
                            continuation.resume(true)
                        }

                        override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                            updateIsOpenAppAdLoading(false)
                            Timber.tag(LOG_TAG).d("onAdFailedToLoad: " + loadAdError.message)
                            continuation.resume(false)
                        }
                    }
                )
            }
        }

        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        private fun isAdAvailable(): Boolean {
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }

        fun showAdIfAvailable(context: Context) {
            showAdIfAvailable(
                context,
                object : OnShowAdCompleteListener {
                    override fun onShowAdComplete() {
                        // Empty because the user will go back to the activity that shows the ad.
                    }
                }
            )
        }

        fun showAdIfAvailable(context: Context, onShowAdCompleteListener: OnShowAdCompleteListener) {
            Timber.tag(LOG_TAG).d("showAdIfAvailable entry")
            if (isShowingAd) {
                Timber.tag(LOG_TAG).d("The app open ad is already showing.")
                return
            }

            if (!isAdAvailable()) {
                Timber.tag(LOG_TAG).d("The app open ad is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                launchCoroutine {
                    Timber.tag(LOG_TAG).d("scope is active: ${this.isActive}")
                    loadAd(context = context)
                }
                return
            }

            Timber.tag(LOG_TAG).d("Will show ad.")

            appOpenAd!!.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    appOpenAd = null
                    isShowingAd = false
                    Timber.tag(LOG_TAG).d("onAdDismissedFullScreenContent.")
                    launchCoroutine {
                        Timber.tag(LOG_TAG).d("scope is active: ${this.isActive}")
                        loadAd(context = context)
                    }
                    onShowAdCompleteListener.onShowAdComplete()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    isShowingAd = false
                    launchCoroutine {
                        Timber.tag(LOG_TAG).d("scope is active: ${this.isActive}")
                        loadAd(context = context)
                    }
                    Timber.tag(LOG_TAG).d("onAdFailedToShowFullScreenContent: %s , scope is active: ${scope.isActive}", adError.message)
                    onShowAdCompleteListener.onShowAdComplete()
                }

                override fun onAdShowedFullScreenContent() {
                    Timber.tag(LOG_TAG).d("onAdShowedFullScreenContent.")
                }
            }
            isShowingAd = true
            currentActivity?.let { appOpenAd!!.show(it) }
        }
    }

    fun updateIsOpenAppAdLoading(isLoading: Boolean) {
        Timber.tag(LOG_TAG).d("updateIsOpenAppAdLoading: $isLoading")
        isOpenAppAdLoading.update { isLoading }
    }

    private fun launchCoroutine(
        context: CoroutineDispatcher = Dispatchers.Main.immediate,
        start: CoroutineStart = CoroutineStart.DEFAULT,
        block: suspend CoroutineScope.() -> Unit,
    ) {
        scope.launch(context, start, block)
    }

    companion object {
        private val AD_UNIT_ID: String
            get() {
                Timber.tag("IOpenAppAdImpl").d("OPEN_AD_UNIT_ID: ${BuildConfig.OPEN_AD_UNIT_ID}")
                return BuildConfig.OPEN_AD_UNIT_ID
            }
        const val LOG_TAG = "MainApplication"
        private var isPreventShowingAdsWhenChangingConfigurations = false
    }

}