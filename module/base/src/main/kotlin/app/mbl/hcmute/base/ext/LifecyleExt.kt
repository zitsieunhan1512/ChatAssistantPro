package app.mbl.hcmute.base.ext

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


fun <T> Fragment.observe(liveData: LiveData<T>?, observer: (T) -> Unit) =
    liveData?.observe(viewLifecycleOwner, Observer(observer))

fun <T> LifecycleOwner.observe(flow: Flow<T>, observer: (T) -> Unit) =
    lifecycleScope.launch {
        flow
            .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
            .collect(observer)
    }


fun <T> FragmentActivity.observe(liveData: LiveData<T>?, observer: (T) -> Unit) =
    liveData?.observe(this, Observer(observer))



