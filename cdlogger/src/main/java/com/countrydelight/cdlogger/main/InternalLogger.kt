package com.countrydelight.cdlogger.main

import android.app.Application
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import com.countrydelight.cdlogger.data.local.event.EventEntity
import com.countrydelight.cdlogger.domain.models.DeviceDetails
import com.countrydelight.cdlogger.domain.models.Event
import com.countrydelight.cdlogger.domain.models.SpaceDetails
import com.countrydelight.cdlogger.domain.usecases.AddEventToLocalUseCase
import com.countrydelight.cdlogger.domain.usecases.StartLogEventWorkerUseCase
import com.countrydelight.cdlogger.domain.utils.SharedPreferenceHelper
import com.countrydelight.cdlogger.main.screen_detectors.ActivityLifecycleDetector
import com.countrydelight.cdlogger.main.utils.FunctionHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID

internal class InternalLogger(
    private val application: Application,
    private val spaceDetails: SpaceDetails?,
    private val logActivityOpeningEvent: Boolean,
    private val logFragmentOpeningEvent: Boolean
) {
    private val preferences = SharedPreferenceHelper.get(application)
    private val addEventToLocalUseCase = AddEventToLocalUseCase(application)

    companion object {
        internal var instance: InternalLogger? = null
    }

    init {
        initLogger()
    }

    /**
     * Initializes the logger.
     */
    private fun initLogger() {
        getAppName()
        generateUUID()
        getDeviceDetails()
        getAdvertisingId()
        setAutoObserver()
        saveSpaceData()
        instance = this
        StartLogEventWorkerUseCase.invoke(application)
    }


    /**
     * Saves the space data to the preferences.
     */
    private fun saveSpaceData() {
        preferences.spaceDetails = spaceDetails ?: SpaceDetails(
            "AAAALNQtN6k",
            "AIzaSyDdI0hCZtE6vySjMm-WEfRq3CPzqKqqsHI",
            "12eQX3gUIi7DdVJsv50ozfQwCX_k-yqjEEiCBTmGGW4"
        )
    }


    /**
     * Sets up an automatic observer for activity and fragment openings.
     *
     * This method registers an ActivityLifecycleCallbacks object to track activity and fragment openings.
     * If either `logActivityOpeningEvent` or `logFragmentOpeningEvent` is true, the observer will be registered.
     */
    private fun setAutoObserver() {
        if (logActivityOpeningEvent || logFragmentOpeningEvent) {
            application.registerActivityLifecycleCallbacks(
                ActivityLifecycleDetector(
                    logFragmentOpeningEvent, logActivityOpeningEvent
                )
            )
        }
    }


    /**
     * Gets the application name from the given application object.
     */
    private fun getAppName() {
        val appLabel = try {
            val appInfo = application.packageManager.getApplicationInfo(application.packageName, 0)
            application.packageManager.getApplicationLabel(appInfo).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
            ""
        }
        preferences.appName = appLabel
    }

    /**
     * Gets the advertising ID from the device.
     */
    private fun getAdvertisingId() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val contentResolver = application.contentResolver
                val advertisingId = Settings.Secure.getString(contentResolver, "advertising_id")
                Log.e("abc", "getAdvertisingId: $advertisingId")
                preferences.advertisingId = advertisingId
            } catch (e: Exception) {
                Log.e("abc", "getAdvertisingId: ${e.localizedMessage}")
            }
        }
    }


    /**
     * Generates a UUID and stores it in the preferences if it is not already set.
     */
    private fun generateUUID() {
        if (preferences.appUID.isBlank()) {
            preferences.appUID = UUID.randomUUID().toString()
        }
    }

    /**
     * Gets the device details.
     *
     * If the device details are not already stored in the preferences, they are retrieved from the
     * Build object and stored in the preferences.
     */
    private fun getDeviceDetails() {
        if (preferences.deviceDetails == null) {
            preferences.deviceDetails = DeviceDetails(
                Build.DEVICE,
                Build.MODEL,
                Build.BRAND,
                Build.MANUFACTURER,
                Build.VERSION.SDK_INT
            )
        }
    }

    internal fun logEvent(event: Event) {
        CoroutineScope(Dispatchers.IO).launch {
            val eventEntity = EventEntity(
                eventName = event.eventName,
                eventData = event.eventData,
                createdAt = FunctionHelper.getCurrentTimeInMillis()
            )
            addEventToLocalUseCase(eventEntity)
            StartLogEventWorkerUseCase.invoke(application)
        }
    }

    internal fun addUserDetails(userDetails: MutableMap<String, Any>) {
        preferences.userDetails = userDetails
    }
}