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
import com.countrydelight.cdlogger.main.detectors.exception.UncaughtExceptionDetector
import com.countrydelight.cdlogger.main.detectors.screen.ActivityLifecycleDetector
import com.countrydelight.cdlogger.main.utils.FunctionHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.UUID
import kotlin.system.exitProcess


/**
 * Internal Logger is responsible for handling logging functionality within the CDLogger library.
 * It manages event logging, crash data logging, and other related tasks.
 *
 * @property application The application context.
 * @property spaceDetails The details of the Google Space where events are logged.
 * @property logActivityOpeningEvent Whether to log activity opening events.
 * @property logFragmentOpeningEvent Whether to log fragment opening events.
 * @property logCrashData Whether to log crash events.
 */
internal class InternalLogger(
    private val application: Application,
    private val spaceDetails: SpaceDetails,
    private val logActivityOpeningEvent: Boolean,
    private val logFragmentOpeningEvent: Boolean,
    private val logCrashData: Boolean
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
        initExceptionHandler()
        instance = this
        StartLogEventWorkerUseCase.invoke(application)
    }


    /**
     * Initializes the custom uncaught exception handler if crash data logging is enabled.
     * If crash data logging is disabled, no custom exception handler will be set.
     */
    private fun initExceptionHandler() {
        if (logCrashData) {
            Thread.setDefaultUncaughtExceptionHandler(UncaughtExceptionDetector())
        }
    }


    /**
     * Saves the space data to the preferences.
     */
    private fun saveSpaceData() {
        preferences.spaceDetails = spaceDetails
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


    /**
     * Logs an event asynchronously and schedules a worker to upload the event to the server.
     * This method is intended for logging events without closing the application.
     *
     * @param event The event to be logged.
     */
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


    /**
     * Logs an event asynchronously and closes the application.
     * This method should be used for logging critical events that require the application to be closed immediately.
     *
     * @param event The event to be logged.
     */
    internal fun logEventAndCloseApp(event: Event) {
        CoroutineScope(Dispatchers.IO).launch {
            val eventEntity = EventEntity(
                eventName = event.eventName,
                eventData = event.eventData,
                createdAt = FunctionHelper.getCurrentTimeInMillis()
            )
            addEventToLocalUseCase(eventEntity)
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(10)
        }
    }


    /**
     * Adds user details to the shared preferences.
     *
     * @param userDetails A mutable map containing user details.
     */
    internal fun addUserDetails(userDetails: MutableMap<String, Any>) {
        preferences.userDetails = userDetails
    }
}