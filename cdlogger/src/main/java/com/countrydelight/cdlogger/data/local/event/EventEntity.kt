package com.countrydelight.cdlogger.data.local.event

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.countrydelight.cdlogger.data.remote.event.EventRemoteEntity
import com.countrydelight.cdlogger.data.utils.DataFunctionHelper
import com.countrydelight.cdlogger.domain.models.Event


/**
 * Represents an event entity in the database.
 *
 * @property id The unique identifier of the event.
 * @property eventName The name of the event.
 * @property eventData A mutable map containing the event data.
 * @property createdAt The timestamp when the event was created.
 */
@Entity
internal data class EventEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val eventName: String,
    val eventData: MutableMap<String, Any>,
    val createdAt: Long
) {

    /**
     * Converts the event entity to an Event object.
     *
     * @return The Event object.
     */
    fun toEvent(): Event {
        return Event(eventName = eventName, eventData = eventData)
    }

    /**
     * Converts the event entity to an EventRemoteEntity object.
     *
     * @return The EventRemoteEntity object.
     */
    fun toEventRemoteEntity(): EventRemoteEntity {
        return EventRemoteEntity(
            eventName = eventName,
            eventData = eventData,
            createdAt = DataFunctionHelper.formatTimeInMillis(createdAt)
        )
    }
}
