package com.countrydelight.cdlogger.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * A class that contains type converters for the CDLogger library.
 */
internal class CDLoggerTypeConverters {

    @TypeConverter
    fun fromMapOfStringAnyToString(mapOfStringAny: MutableMap<String, Any>): String {
        return Gson().toJson(mapOfStringAny)

    }

    @TypeConverter
    fun toMapToStringAnyFromString(stringOfMap: String): MutableMap<String, Any> {
        return Gson().fromJson(
            stringOfMap,
            object : TypeToken<MutableMap<String, Any>>() {}.type
        )
    }
}