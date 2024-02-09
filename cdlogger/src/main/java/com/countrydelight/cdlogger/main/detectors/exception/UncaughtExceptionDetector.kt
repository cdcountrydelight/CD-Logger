package com.countrydelight.cdlogger.main.detectors.exception

import com.countrydelight.cdlogger.domain.models.Event
import com.countrydelight.cdlogger.main.InternalLogger
import com.countrydelight.cdlogger.main.utils.ConstantHelper
import java.lang.Thread.UncaughtExceptionHandler

class UncaughtExceptionDetector : UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        InternalLogger.instance?.logEventAndCloseApp(
            Event(
                ConstantHelper.APP_EXCEPTION, mutableMapOf(
                    Pair(ConstantHelper.EXCEPTION_MESSAGE, e.localizedMessage ?: ""),
                    Pair(ConstantHelper.EXCEPTION_STACK_TRACE, e.stackTrace)
                )
            )
        )
    }
}