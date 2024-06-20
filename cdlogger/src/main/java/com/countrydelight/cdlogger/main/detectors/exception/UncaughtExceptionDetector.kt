package com.countrydelight.cdlogger.main.detectors.exception

import com.countrydelight.cdlogger.main.InternalLogger
import com.countrydelight.cdlogger.main.utils.ConstantHelper
import java.lang.Thread.UncaughtExceptionHandler
import kotlin.math.min

class UncaughtExceptionDetector : UncaughtExceptionHandler {
    override fun uncaughtException(thread: Thread, exception: Throwable) {
        val stackTrace = exception.stackTrace
        val stackTraceMessage = if (stackTrace.isNotEmpty()) {
            stackTrace.slice(0..min(2, stackTrace.lastIndex))
        } else {
            emptyList()
        }
        InternalLogger.instance?.logEventAndCloseApp(
            ConstantHelper.APP_EXCEPTION, mutableMapOf(
                ConstantHelper.EXCEPTION_MESSAGE to (exception.message ?: "N/A"),
                ConstantHelper.EXCEPTION_STACK_TRACE to stackTraceMessage
            )
        )
    }
}