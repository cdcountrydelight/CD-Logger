package com.countrydelight.countrydelightlogger

import android.app.Application
import com.countrydelight.cdlogger.domain.models.SpaceDetails
import com.countrydelight.cdlogger.main.CDLogger

class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()
        CDLogger.Builder(
            this, SpaceDetails(
                "AAAALNQtN6k",
                "AIzaSyDdI0hCZtE6vySjMm-WEfRq3CPzqKqqsHI",
                "12eQX3gUIi7DdVJsv50ozfQwCX_k-yqjEEiCBTmGGW4"
            )
        ).logActivityOpeningEvent(true)
            .build()
    }
}