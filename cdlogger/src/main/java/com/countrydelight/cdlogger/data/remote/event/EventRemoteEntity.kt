package com.countrydelight.cdlogger.data.remote.event


/**
 * Represents an event entity from a remote source.
 *
 * @property eventName The name of the event.
 * @property eventData A mutable map containing the event data.
 * @property createdAt The date and time when the event was created.
 */
internal data class EventRemoteEntity(
    val eventName: String,
    val eventData: MutableMap<String, Any>,
    val createdAt: String
)