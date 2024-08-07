package com.countrydelight.cdlogger.data.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

internal object DataFunctionHelper {


    /**
     * Formats a timestamp in milliseconds to a human-readable string.
     *
     * @param millis The timestamp in milliseconds.
     * @param format The date format string. Defaults to "dd MMM yyyy hh:mm:ss a".
     * @return The formatted timestamp.
     */
    fun formatTimeInMillis(millis: Long, format: String = "dd MMM yyyy hh:mm:ss a"): String {
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        val date = Date(millis)
        return sdf.format(date)
    }

}