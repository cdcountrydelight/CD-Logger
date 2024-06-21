package com.countrydelight.cdlogger.main

import android.app.Application
import com.countrydelight.cdlogger.base.utils.ILoggerFailureCallback
import com.countrydelight.cdlogger.domain.models.SpaceDetails


/**
 * A helper class for logging events.
 */
class CDLogger {

    companion object {

        /**
         * Checks if the logger is initialized.
         *
         * @return `true` if the logger is initialized, `false` otherwise.
         */
        fun isLoggerInitialized() = InternalLogger.instance != null


        /**
         * Logs an event with the provided event name and event message.
         *
         * @param eventName The name of the event to log.
         * @param eventMessage The message associated with the event.
         */
        fun logEvent(eventName: String, eventMessage: String) {
            InternalLogger.instance?.logEvent(eventName, eventMessage)
        }


        /**
         * Logs an event with the provided event message.
         *
         * @param eventMessage The message to be logged.
         */
        fun logEvent(eventMessage: String) {
            InternalLogger.instance?.logEvent(null, eventMessage)
        }


        /**
         * Logs an event with the provided event name and event data.
         *
         * @param eventName The name of the event to log.
         * @param eventData A mutable map containing additional data associated with the event.
         */
        fun logEvent(eventName: String, eventData: MutableMap<String, Any>) {
            InternalLogger.instance?.logEvent(eventName, eventData)
        }


        /**
         * Will log user details using the application.
         *
         * @param userDetails A mutable map containing user details.
         */
        fun addUserDetails(userDetails: MutableMap<String, Any>) {
            InternalLogger.instance?.addUserDetails(userDetails)
        }
    }

    /**
     * A builder class for creating a CDLogger instance.
     *
     * @property application The application context.
     * @property spaceDetails The space details ,logs data to the specified google space
     */
    class Builder(
        private val application: Application,
        private val spaceDetails: SpaceDetails
    ) {
        private var logActivityOpeningEvent: Boolean = false
        private var logFragmentOpeningEvent: Boolean = false
        private var logCrashData: Boolean = true
        private var loggerFailureCallback: ILoggerFailureCallback? = null

        /**
         * Sets whether to log activity opening events.
         *
         * By default, activity opening events are not logged.
         * @param logActivityOpeningEvent True to log activity opening events, false otherwise.
         * @return The builder instance.
         */
        fun logActivityOpeningEvent(logActivityOpeningEvent: Boolean): Builder {
            this.logActivityOpeningEvent = logActivityOpeningEvent
            return this
        }


        /**
         * Registers a callback to handle logger failure events.
         *
         * @param callback The callback to handle logger failure events.
         * @return This Builder instance to allow for method chaining.
         */
        fun registerLoggerFailureCallbacks(callback: ILoggerFailureCallback): Builder {
            this.loggerFailureCallback = callback
            return this
        }

        /**
         * Sets whether to log fragment opening events.
         *
         * By default, fragment opening events are not logged.
         * @param logFragmentOpeningEvent when set true it automatically logs the fragment opening event if the parent activity is AppCompatActivity or FragmentActivity.
         * @return The builder instance.
         */
        fun logFragmentOpeningEvent(logFragmentOpeningEvent: Boolean): Builder {
            this.logFragmentOpeningEvent = logFragmentOpeningEvent
            return this
        }

        /**
         * Sets whether to log crash events.
         *
         * By default, crash events are automatically logged.
         *
         * @param logCrashEvent True to enable logging of crash events, false otherwise.
         * @return The builder instance.
         */
        fun logCrashEvent(logCrashEvent: Boolean): Builder {
            this.logCrashData = logCrashEvent
            return this
        }


        /**
         * Builds  a new CDLogger instance.
         */
        fun build() {
            InternalLogger(
                application = application,
                spaceDetails = spaceDetails,
                logActivityOpeningEvent = logActivityOpeningEvent,
                logFragmentOpeningEvent = logFragmentOpeningEvent,
                logCrashData = logCrashData,
                loggerFailureCallback = loggerFailureCallback
            )
        }
    }
}