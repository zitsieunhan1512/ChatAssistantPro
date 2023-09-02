package app.mbl.hcmute.base.ext

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.takeWhile

fun <T> Flow<T>.takeUntilTimeout(durationMillis: Long): Flow<T> = flow {
    val endTime = System.currentTimeMillis() + durationMillis
    takeWhile { System.currentTimeMillis() >= endTime }
        .collect {
            emit(it)
        }
}

fun <T> Flow<T>.handleErrors(action: (Throwable) -> Unit): Flow<T> =
    catch { e -> action.invoke(e) }