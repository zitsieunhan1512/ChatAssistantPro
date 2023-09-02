package app.mbl.hcmute.base.admob.openAppAd

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import kotlinx.coroutines.flow.MutableStateFlow

interface IOpenAppAd : Application.ActivityLifecycleCallbacks, DefaultLifecycleObserver {
    val isOpenAppAdLoading: MutableStateFlow<Boolean>
    fun configOpenAppAd(application: Application, lifecycle: Lifecycle)
}