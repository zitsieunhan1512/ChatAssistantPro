package app.mbl.hcmute.base.admob

import android.content.Context
import app.mbl.hcmute.base.BuildConfig
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class IAdMobImpl : IAdMob {
    //todo: change this to your own test device id
    override fun configureAdMob(context: Context) {
        MobileAds.initialize(context) {}
        val builder = RequestConfiguration.Builder()
        if (BuildConfig.DEBUG) {
            builder.setTestDeviceIds(listOf("ABCDEF012345"))
        }
        MobileAds.setRequestConfiguration(
            builder.build()
        )
    }
}