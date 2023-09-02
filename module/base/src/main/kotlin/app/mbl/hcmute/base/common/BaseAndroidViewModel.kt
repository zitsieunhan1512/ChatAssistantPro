package app.mbl.hcmute.base.common

import android.app.Application
import androidx.lifecycle.*
import app.mbl.hcmute.base.event.SingleLiveEvent
import app.mbl.hcmute.base.ext.getTagName
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.*
import timber.log.Timber

abstract class BaseAndroidViewModel(application: Application) : AndroidViewModel(application) {
    private val mCompositeDisposable by lazy { CompositeDisposable() }

    private val viewModelJob = SupervisorJob()
    protected val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    var showCommonProgressBar = SingleLiveEvent<Boolean>()
    var errorMessage = SingleLiveEvent<String>()
    var exceptionError: AppException? = null

    protected val _uiState = MutableLiveData<UIState>()
    val uiState: LiveData<UIState>
        get() = _uiState

    val uiSingleEvent: SingleLiveEvent<UIState> by lazy { SingleLiveEvent() }
    protected val _clickEvent = SingleLiveEvent<UIState>()

    val clickEvent: LiveData<UIState>
        get() = _clickEvent

    val handlerCoroutineException = CoroutineExceptionHandler { _, exception ->
        manageException(exception)
    }
    protected val TAG: String by getTagName()

    open fun manageCoroutineException(exception: Any) {
        viewModelScope.launch {
            if (exception is AppException) {

                errorMessage.value = exception.message

            } else if (exception is Throwable) {
                errorMessage.value = exception.localizedMessage
            }
            showCommonProgressBar.value = false
        }

    }

    open fun manageException(exception: Any) {
        showCommonProgressBar.value = false
    }

    open fun addSubscribe(disposable: Disposable) {
        mCompositeDisposable.add(disposable)
    }

    override fun onCleared() {
        super.onCleared()
        Timber.d("onCleared", "Enter")
        mCompositeDisposable.clear()
        viewModelJob.cancel()
    }

    open fun onDestroyView() {}
}