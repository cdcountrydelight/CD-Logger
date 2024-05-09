package com.countrydelight.cdlogger.base.utils

/**
 * Interface definition for a callback to handle logger failure events.
 *
 * Implement this interface to handle logger failure events.
 */
interface ILoggerFailureCallback {

    /**
     * Called when a logger failure event occurs.
     *
     * @param failureTag A tag indicating the type or context of the logger failure.
     * @param exception The exception representing the logger failure.
     */
    fun onLoggerFailure(failureTag: String, exception: Exception)
}