package app.mbl.hcmute.base.ext

import androidx.lifecycle.viewModelScope
import app.mbl.hcmute.base.common.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.retryWhen
import timber.log.Timber
import java.io.IOException
import kotlin.reflect.KClass

suspend fun <T> retryIO(times: Int = Int.MAX_VALUE, initialDelay: Long = 100, maxDelay: Long = 1000, factor: Double = 2.0, block: suspend () -> T): T {
    var currentDelay = initialDelay
    repeat(times - 1) {
        try {
            return block()
        } catch (e: IOException) {
            // you can log an error here and/or make a more finer-grained
            // analysis of the cause to see if retry is needed
            println("Caught ${e}, message=`${e.message}`. Retrying ")
        }
        delay(currentDelay)
        currentDelay = (currentDelay * factor).toLong().coerceAtMost(maxDelay)
    }
    return block() // last attempt
}

suspend fun <T> retryIfThrown(times: Int, exceptionType: KClass<out Throwable>, initialDelay: Long = 100, block: suspend () -> T): T {
    for (i in 0 until times) {
        try {
            delay(initialDelay)
            return block()
        } catch (e: Throwable) {
            if (e.javaClass.kotlin == exceptionType && i < times - 1) {
                println("Caught ${exceptionType.simpleName}, message=`${e.message}`. Retrying ${i + 1}/$times")
            } else {
                throw e
            }
        }
    }
    return block()
}

suspend fun <T> Deferred<T>.processRequest(onSuccess: (T) -> Unit = {}, onFail: () -> Unit = {}): Deferred<T> {
    return withContext(Dispatchers.IO) {
        async {
            val respone = await()
            if (respone != null) {
                onSuccess.invoke(respone)
            } else {
                onFail.invoke()
            }
            respone

        }
    }
}

suspend fun <T> Flow<T>.processRequestFlow(
    delayTime: Long = 2000, retryTimes: Int = 0, onFail: (Throwable) -> Unit = {},
    onRetry: (Throwable, Long) -> Unit = { _, _ -> },
): Flow<T> {

    return retryWhen { cause, attempt ->
        val isRetry = attempt < retryTimes
        if (isRetry) {
            val TAG = this@processRequestFlow::class.java.simpleName
            Timber.d(TAG, "processRequest", "retrying ${attempt + 1} times... sendRequest ${cause.message}")
            onRetry.invoke(cause, attempt)
        }
        delay(delayTime)
        isRetry
    }.catch {
        onFail.invoke(it)
    }
}

// use this when need call suspend function inside Unit function
suspend fun <T> Flow<T>.processRequestWithSuspend(
    delayTime: Long = 2000, retryTimes: Int = 1, onFail: suspend (Throwable) -> Unit = {},
    onRetry: suspend (Throwable, Long) -> Unit = { _, _ -> },
): Flow<T> {

    return retryWhen { cause, attempt ->
        val isRetry = attempt < retryTimes
        if (isRetry) onRetry.invoke(cause, attempt)
        delay(delayTime)
        isRetry
    }.catch {
        onFail.invoke(it)
    }
}

fun Job.cancelWork() = run { if (isActive) cancel() }

fun BaseViewModel.launchIO(action: suspend () -> Unit, onFail: (Throwable) -> Unit = {}) {
    val exception = CoroutineExceptionHandler { _, exception -> onFail.invoke(exception) }.plus(Dispatchers.IO)
    viewModelScope.launch(exception) {
        action.invoke()
    }
}

fun BaseViewModel.launch(action: suspend () -> Unit, onFail: (Throwable) -> Unit = {}) {
    val exception = CoroutineExceptionHandler { _, exception -> onFail.invoke(exception) }
    viewModelScope.launch(exception) {
        action.invoke()
    }
}

fun <T> CoroutineScope.lazyCoroutine(block: suspend CoroutineScope.() -> T): Deferred<T> {
    return async(start = CoroutineStart.LAZY) { block() }
}

