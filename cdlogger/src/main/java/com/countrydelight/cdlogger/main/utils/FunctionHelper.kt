package com.countrydelight.cdlogger.main.utils

import com.countrydelight.cdlogger.main.InternalLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * contains helper function for quick development.
 */
internal object FunctionHelper {

    /**
     * Returns the current time in milliseconds.
     *
     * @return The current time in milliseconds.
     */
    fun getCurrentTimeInMillis(): Long {
        return System.currentTimeMillis()
    }

    /**
     * Executes a suspended function in the background using a coroutine.
     *
     * This function launches a coroutine in the IO dispatcher to execute the provided suspended function.
     * It catches any exceptions thrown during the execution of the suspended function and logs them.
     *
     * @param call The suspended function to be executed in the background.
     * @param onCompletion A lambda function to be executed upon completion of the background call. Default is an empty lambda.
     * @param logMessageTag The tag to be used in the log message for any caught exceptions. Default is "Background Call Failed with message".
     */
    internal fun backgroundCall(
        call: suspend () -> Unit,
        onCompletion: () -> Unit = {},
        logMessageTag: String = "Background Call"
    ) {
        CoroutineScope(InternalLogger.backgroundThread).launch {
            try {
                call()
            } catch (exception: Exception) {
                InternalLogger.loggerFailureCallback?.onLoggerFailure(logMessageTag, exception)
            }
        }.invokeOnCompletion {
            onCompletion()
        }
    }


}

