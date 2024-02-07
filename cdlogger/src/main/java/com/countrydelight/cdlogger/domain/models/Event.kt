package com.countrydelight.cdlogger.domain.models


/**
 * Represents an event.
 * @property eventName The name of the event.
 * @property eventData A mutable map containing the event data.
 */
data class Event(
    val eventName: String,
    val eventData: MutableMap<String, Any>,
)