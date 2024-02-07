package com.countrydelight.countrydelightlogger

import android.app.Application
import com.countrydelight.cdlogger.main.CDLogger

class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CDLogger.Builder(this)
            .logActivityOpeningEvent(true)
            .build()
    }
}