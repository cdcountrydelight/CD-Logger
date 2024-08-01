package com.countrydelight.cdlogger.main

import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.content.pm.PackageInfoCompat
import com.countrydelight.cdlogger.base.utils.BaseConstantHelper
import com.countrydelight.cdlogger.base.utils.ILoggerFailureCallback
import com.countrydelight.cdlogger.base.utils.SharedPreferenceHelper
import com.countrydelight.cdlogger.data.local.event.EventEntity
import com.countrydelight.cdlogger.domain.models.SpaceDetails
import com.countrydelight.cdlogger.domain.usecases.AddEventToLocalUseCase
import com.countrydelight.cdlogger.domain.usecases.StartLogEventWorkerUseCase
import com.countrydelight.cdlogger.main.beans.AppMetaDataBean
import com.countrydelight.cdlogger.main.detectors.exception.UncaughtExceptionDetector
import com.countrydelight.cdlogger.main.detectors.screen.ActivityLifecycleDetector
import com.countrydelight.cdlogger.main.utils.ConstantHelper.MESSAGE
import com.countrydelight.cdlogger.main.utils.ConstantHelper.PLAY_SERVICE_NOT_AVAILABLE
import com.countrydelight.cdlogger.main.utils.FunctionHelper
import com.countrydelight.cdlogger.main.utils.FunctionHelper.backgroundCall
import com.countrydelight.cdlogger.main.utils.FunctionHelper.isGooglePLayServiceAvailable
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.UUID
import java.util.concurrent.Executors
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
    private val logCrashData: Boolean,
    private val loggerFailureCallback: ILoggerFailureCallback?
) {
    private val preferences = SharedPreferenceHelper.get(application)
    private val addEventToLocalUseCase = AddEventToLocalUseCase(application)

    companion object {
        internal var instance: InternalLogger? = null
        internal var loggerFailureCallback: ILoggerFailureCallback? = null
        internal val backgroundThread = Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    }


    init {
        initLogger()
    }


    /**
     * Initializes the logger.
     */
    private fun initLogger() {
        InternalLogger.loggerFailureCallback = loggerFailureCallback
        getAppName()
        generateUUID()
        getDeviceDetails()
        getAdvertisingId()
        setAutoObserver()
        saveSpaceData()
        initExceptionHandler()
        instance = this
        StartLogEventWorkerUseCase.invoke(application)
        Log.i(BaseConstantHelper.LOG_TAG, "CDLogger Started...")
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
        if (preferences.appName.isBlank()) {
            val appName = try {
                val appInfo =
                    application.packageManager.getApplicationInfo(application.packageName, 0)
                application.packageManager.getApplicationLabel(appInfo).toString()
            } catch (exception: PackageManager.NameNotFoundException) {
                loggerFailureCallback?.onLoggerFailure("App Name", exception)
                ""
            }
            preferences.appName = appName
        }
    }

    /**
     * Gets the advertising ID from the device.
     */
    private fun getAdvertisingId() {
        if (preferences.advertisingId.isNullOrBlank() || preferences.advertisingId == PLAY_SERVICE_NOT_AVAILABLE) {
            backgroundCall(call = {
                preferences.advertisingId = if (application.isGooglePLayServiceAvailable()) {
                    AdvertisingIdClient.getAdvertisingIdInfo(application).id
                } else {
                    PLAY_SERVICE_NOT_AVAILABLE
                }
            }, logMessageTag = "Advertising Id")
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
            val deviceDetails = mutableMapOf<String, Any>()
            deviceDetails[BaseConstantHelper.DEVICE_NAME] = Build.DEVICE
            deviceDetails[BaseConstantHelper.DEVICE_MODEL] = Build.MODEL
            deviceDetails[BaseConstantHelper.DEVICE_BRAND] = Build.BRAND
            deviceDetails[BaseConstantHelper.DEVICE_MANUFACTURER] = Build.MANUFACTURER
            deviceDetails[BaseConstantHelper.DEVICE_SDK_INT] = Build.VERSION.SDK_INT
            preferences.deviceDetails = deviceDetails
        }
    }


    /**
     * Logs an event internally with the provided event name and event data.
     *
     * @param eventName The name of the event to log.
     * @param eventData Additional data associated with the event.
     */
    private suspend fun logEventInternally(eventName: String?, eventData: MutableMap<String, Any>) {
        val appMetaData = getAppMetaData()
        val eventEntity = EventEntity(
            eventName = eventName,
            eventData = eventData,
            createdAt = FunctionHelper.getCurrentTimeInMillis(),
            appVersionName = appMetaData?.versionName,
            appVersionCode = appMetaData?.versionCode
        )
        withContext(Dispatchers.IO) {
            addEventToLocalUseCase(eventEntity)
            StartLogEventWorkerUseCase.invoke(application)
        }
    }


    /**
     * Retrieves the application metadata including version name and version code.
     *
     * @return AppMetaDataBean containing the app version name and version code, or null if an exception occurs.
     */
    private fun getAppMetaData(): AppMetaDataBean? {
        return try {
            val packageInfo: PackageInfo =
                application.packageManager.getPackageInfo(application.packageName, 0)
            val appVersionName = packageInfo.versionName
            val appVersionCode = PackageInfoCompat.getLongVersionCode(packageInfo)
            AppMetaDataBean(appVersionName, appVersionCode)
        } catch (exception: Exception) {
            loggerFailureCallback?.onLoggerFailure("App Meta Data", exception)
            null
        }
    }

    /**
     * Logs an event with the provided event name and event message.
     *
     * This function internally calls [logEventInternally] to log the event with the given name and message.
     * The event message is stored in the event data under the key [MESSAGE].
     *
     * @param eventName The name of the event to log.
     * @param eventMessage The message associated with the event.
     */
    internal fun logEvent(eventName: String?, eventMessage: String) {
        backgroundCall(call = {
            logEventInternally(eventName, mutableMapOf(MESSAGE to eventMessage))
        })
    }


    /**
     * Logs an event with the provided event name and event data.
     *
     * This function internally calls [logEventInternally] to log the event with the given name and data.
     *
     * @param eventName The name of the event to log.
     * @param eventData A mutable map containing additional data associated with the event.
     */
    internal fun logEvent(eventName: String?, eventData: MutableMap<String, Any>) {
        backgroundCall(call = {
            logEventInternally(eventName, eventData)
        })
    }


    /**
     * Logs an event with the provided event name and event data, then closes the application.
     *
     * This function internally calls [logEventInternally] to log the event with the given name and data.
     * After logging the event, it forcefully terminates the application process.
     *
     * @param eventName The name of the event to log.
     * @param eventData A mutable map containing additional data associated with the event.
     */
    internal fun logEventAndCloseApp(eventName: String, eventData: MutableMap<String, Any>) {
        backgroundCall(call = {
            logEventInternally(eventName, eventData)
        }, onCompletion = {
            android.os.Process.killProcess(android.os.Process.myPid())
            exitProcess(10)
        })
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