package com.countrydelight.cdlogger.main.detectors.exception

import com.countrydelight.cdlogger.main.InternalLogger
import com.countrydelight.cdlogger.main.utils.ConstantHelper
import java.lang.Thread.UncaughtExceptionHandler

class UncaughtExceptionDetector : UncaughtExceptionHandler {
    override fun uncaughtException(t: Thread, e: Throwable) {
        InternalLogger.instance?.logEventAndCloseApp(
            ConstantHelper.APP_EXCEPTION, mutableMapOf(
                ConstantHelper.EXCEPTION_MESSAGE to (e.localizedMessage?.toString() ?: ""),
                ConstantHelper.EXCEPTION_STACK_TRACE to e.stackTrace
            )
        )
    }
}