package app.mbl.hcmute.base.admob.rewardedVideoAd

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber

class IRewardedVideoAdImpl : IRewardedVideoAd {
    private val TAG = IRewardedVideoAdImpl::class.java.simpleName
    private var rewardedAd: RewardedAd? = null
    lateinit var activity: Activity
    override val isRewardedVideoAdLoading = MutableStateFlow(false)
    override fun configureRewardedVideoAd(activity: Activity) {
        this.activity = activity
        loadRewardedVideoAd(activity)
    }

    private fun loadRewardedVideoAd(activity: Activity) {
        if (rewardedAd == null && !isRewardedVideoAdLoading.value) {
            updateIsRewardedVideoAdLoading(true)
            val adRequest = AdRequest.Builder().build()

            RewardedAd.load(
                activity,
                REWARDED_VIDEO_AD_UNIT_ID,
                adRequest,
                object : RewardedAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        Timber.e("Ad was load failed: ${adError.message}.")
                        updateIsRewardedVideoAdLoading(false)
                        rewardedAd = null
                    }

                    override fun onAdLoaded(ad: RewardedAd) {
                        Timber.d("Ad was loaded.")
                        rewardedAd = ad
                        updateIsRewardedVideoAdLoading(false)
                    }
                }
            )
        }
    }

    override fun showRewardedVideoAd(onUserEarnedRewardListener: (RewardItem) -> Unit, onFailed: () -> Unit) {
        if (rewardedAd != null) {
            rewardedAd?.fullScreenContentCallback =
                object : FullScreenContentCallback() {
                    override fun onAdDismissedFullScreenContent() {
                        Timber.d("Ad was dismissed.")
                        rewardedAd = null
                        loadRewardedVideoAd(activity)
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        Timber.d("Ad failed to show.")
                        rewardedAd = null
                        onFailed()
                    }

                    override fun onAdShowedFullScreenContent() {
                        Timber.d("Ad showed fullscreen content.")
                        // Called when ad is dismissed.
                    }
                }

            rewardedAd?.show(
                activity
            ) { rewardItem ->
                var rewardAmount = rewardItem.amount
                Timber.tag("TAG").d("User earned the reward.")
                onUserEarnedRewardListener(rewardItem)
            }
        } else {
            Timber.d("The rewarded ad wasn't ready yet.")
            onFailed()
        }
    }

    private fun updateIsRewardedVideoAdLoading(isLoading: Boolean) {
        isRewardedVideoAdLoading.update { isLoading }
    }
}