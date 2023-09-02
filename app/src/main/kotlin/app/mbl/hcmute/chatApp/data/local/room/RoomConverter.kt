package app.mbl.hcmute.chatApp.data.local.room

import androidx.room.TypeConverter
import app.mbl.hcmute.chatApp.domain.entities.Author
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class RoomConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromJson(json: String): Author {
        val type = object : TypeToken<Author>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun toJson(obj: Author): String {
        return gson.toJson(obj)
    }

    @TypeConverter
    fun fromDate(date: Date): String {
        return gson.toJson(date)
    }

    @TypeConverter
    fun toDate(json: String): Date {
        val type = object : TypeToken<Date>() {}.type
        return gson.fromJson(json, type)
    }
}