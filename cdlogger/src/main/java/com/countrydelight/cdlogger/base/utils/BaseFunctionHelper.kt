package com.countrydelight.cdlogger.base.utils

object BaseFunctionHelper {
    fun MutableMap<String, Any>.appendMap(map: Map<String, Any>?) {
        if (map == null) return
        for (data in map) {
            this[data.key] = data.value
        }
    }
}