package com.countrydelight.cdlogger.base.utils

import android.content.Context
import android.content.SharedPreferences
import com.countrydelight.cdlogger.domain.models.SpaceDetails
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


/**
 * A helper class for interacting with shared preferences.
 **/
internal class SharedPreferenceHelper private constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(
            "com.countrydelight.cdlogger.shared_preference",
            Context.MODE_PRIVATE
        )

    companion object {
        private const val APP_UID = "app_uid"
        private const val DEVICE_DETAILS = "device_details"
        private const val ADVERTISING_ID = "advertising_id"
        private const val APP_NAME = "app_name"
        private const val SPACE_DATA = "space_data"
        private const val USER_DETAILS = "user_details"

        @Volatile
        private var instance: SharedPreferenceHelper? = null
        fun get(context: Context): SharedPreferenceHelper {
            return instance ?: synchronized(this) {
                instance ?: SharedPreferenceHelper(context).also { instance = it }
            }
        }
    }

    var appUID: String
        get() = sharedPreferences.getString(APP_UID, "") ?: ""
        set(updatedAppUID) {
            sharedPreferences.edit().putString(APP_UID, updatedAppUID).apply()
        }

    var deviceDetails: MutableMap<String, Any>?
        get() {
            val gson = Gson()
            val json = sharedPreferences.getString(DEVICE_DETAILS, null)
            val type = object : TypeToken<MutableMap<String, String>>() {}.type
            return gson.fromJson(json, type)
        }
        set(updatedDeviceDetails) {
            val gson = Gson()
            val json = gson.toJson(updatedDeviceDetails)
            sharedPreferences.edit().putString(DEVICE_DETAILS, json).apply()
        }

    var advertisingId: String?
        get() = sharedPreferences.getString(ADVERTISING_ID, "") ?: ""
        set(updatedAdvertisingId) {
            sharedPreferences.edit().putString(ADVERTISING_ID, updatedAdvertisingId).apply()
        }

    var appName: String
        get() = sharedPreferences.getString(APP_NAME, "") ?: ""
        set(updatedAppName) {
            sharedPreferences.edit().putString(APP_NAME, updatedAppName).apply()
        }

    var userDetails: MutableMap<String, Any>
        get() {
            val gson = Gson()
            val json = sharedPreferences.getString(USER_DETAILS, null)
            val type = object : TypeToken<MutableMap<String, Any>>() {}.type
            return gson.fromJson(json, type) ?: mutableMapOf()
        }
        set(updatedUserDetails) {
            val gson = Gson()
            val json = gson.toJson(updatedUserDetails)
            sharedPreferences.edit().putString(USER_DETAILS, json).apply()
        }

    var spaceDetails: SpaceDetails
        get() {
            val gson = Gson()
            val json = sharedPreferences.getString(SPACE_DATA, null)
            return gson.fromJson(json, SpaceDetails::class.java)
        }
        set(updatedSpaceData) {
            val gson = Gson()
            val json = gson.toJson(updatedSpaceData)
            sharedPreferences.edit().putString(SPACE_DATA, json).apply()
        }
}