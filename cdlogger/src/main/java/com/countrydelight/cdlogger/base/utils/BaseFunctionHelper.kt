package com.countrydelight.cdlogger.base.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object BaseFunctionHelper {

    /**
     * Extension function to append the contents of another map to this mutable map.
     *
     * This function takes another map as an input and adds all key-value pairs from the input map
     * to the mutable map it is called on. If the input map is null, the function does nothing.
     *
     * @param map The map whose key-value pairs are to be added to this mutable map. If null, the function returns without making any changes.
     */
    fun MutableMap<String, Any>.appendMap(map: Map<String, Any>?) {
        if (map == null) return
        for (data in map) {
            this[data.key] = data.value
        }
    }


    /**
     * Extension function to check if the network is available on the device.
     *
     * This function uses the ConnectivityManager to determine if there is an active network connection.
     * It checks for different types of network transport (Wi-Fi, Cellular, Ethernet) to confirm network availability.
     *
     * @return Boolean - true if there is an active network connection, false otherwise.
     */
    fun Context.isNetworkAvailable(): Boolean {
        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkCapabilities = connectivityManager?.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
}