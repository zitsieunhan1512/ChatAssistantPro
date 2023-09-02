package app.mbl.hcmute.base.ext

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.google.gson.JsonElement

inline fun <reified T> Gson.fromJson(jsonElement: JsonElement): T? = this.fromJson<T>(jsonElement, object : com.google.gson.reflect.TypeToken<T>() {}.type)

inline fun <reified T> Gson.fromJson(jsonStr: String?): T? = this.fromJson<T>(jsonStr, object : com.google.gson.reflect.TypeToken<T>() {}.type)

inline fun <reified T> Gson.ToJson(data: T?): String? = this.toJson(data, object : com.google.gson.reflect.TypeToken<T>() {}.type)

inline fun <reified T> String?.toObject(): T? {
    return if (isNullOrEmpty() || this == "[]") null
    else return Gson().fromJson<T>(this)
}

fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    block: (T?, K?) -> R,
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        result.value = block(this.value, liveData.value)
    }
    result.addSource(liveData) {
        result.value = block(this.value, liveData.value)
    }
    return result
}

