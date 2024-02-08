package com.countrydelight.countrydelightlogger

import android.view.View
import android.widget.TextView
import com.countrydelight.cdlogger.domain.models.Event
import com.countrydelight.cdlogger.main.CDLogger

object FunctionHelper {
    fun View.genericOnClick(onClick: () -> Unit) {
        setOnClickListener { view ->
            onClick()
            if (view is TextView) {
                CDLogger.logEvent(Event("View Click", mutableMapOf(Pair("view_name", view.text))))
            } else {
                CDLogger.logEvent(
                    Event(
                        "View Click",
                        mutableMapOf(Pair("view_name", view::class.java.simpleName))
                    )
                )
            }
        }

    }
}