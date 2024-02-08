package com.countrydelight.cdlogger.main

import android.app.Application
import com.countrydelight.cdlogger.domain.models.Event
import com.countrydelight.cdlogger.domain.models.SpaceDetails


/**
 * A helper class for logging events.
 */
class CDLogger {

    companion object {

        /**
         * Logs an event.
         *
         * @param event The event to log.
         */

        fun logEvent(event: Event) {
            InternalLogger.instance?.logEvent(event)
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
     * @property spaceDetails The space details , when provided logs data to the specified google space, else to default google space
     */
    class Builder(
        private val application: Application,
        private val spaceDetails: SpaceDetails
    ) {
        private var logActivityOpeningEvent: Boolean = false
        private var logFragmentOpeningEvent: Boolean = false

        /**
         * Sets whether to log activity opening events.
         *
         * Default value - false
         * @param logActivityOpeningEvent True to log activity opening events, false otherwise.
         * @return The builder instance.
         */
        fun logActivityOpeningEvent(logActivityOpeningEvent: Boolean): Builder {
            this.logActivityOpeningEvent = logActivityOpeningEvent
            return this
        }

        /**
         * Sets whether to log fragment opening events.
         *
         * Default value - false
         * @param logFragmentOpeningEvent when set true it automatically logs the fragment opening event if the parent activity is AppCompatActivity or FragmentActivity.
         * @return The builder instance.
         */
        fun logFragmentOpeningEvent(logFragmentOpeningEvent: Boolean): Builder {
            this.logFragmentOpeningEvent = logFragmentOpeningEvent
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
            )
        }
    }
}