package app.mbl.hcmute.base.admob.rewardedVideoAd

import android.app.Activity
import app.mbl.hcmute.base.BuildConfig
import com.google.android.gms.ads.rewarded.RewardItem
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber

interface IRewardedVideoAd {
    val REWARDED_VIDEO_AD_UNIT_ID: String
        get() {
            Timber.tag("IRewardedVideoAd").d("REWARDED_VIDEO_AD_UNIT_ID: ${BuildConfig.REWARDED_AD_UNIT_ID}")
            return BuildConfig.REWARDED_AD_UNIT_ID
        }
    val isRewardedVideoAdLoading: MutableStateFlow<Boolean>
    fun configureRewardedVideoAd(activity: Activity)
    fun showRewardedVideoAd(onUserEarnedRewardListener: (RewardItem) -> Unit, onFailed: () -> Unit)
}