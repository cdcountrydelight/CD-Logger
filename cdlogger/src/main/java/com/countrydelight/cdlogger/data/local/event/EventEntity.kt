package com.countrydelight.cdlogger.data.local.event

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.countrydelight.cdlogger.base.utils.BaseConstantHelper.CREATED_AT
import com.countrydelight.cdlogger.base.utils.BaseConstantHelper.DETAILS
import com.countrydelight.cdlogger.base.utils.BaseConstantHelper.NAME
import com.countrydelight.cdlogger.base.utils.BaseFunctionHelper.appendMap
import com.countrydelight.cdlogger.data.utils.DataFunctionHelper


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
     * Converts the event to a displayable string format.
     *
     * This function returns a string representation of the event in a displayable format.
     *
     * @return A string representing the event in a displayable format.
     */
    fun toDisplayEvent(): String {
        return "{ \"eventName\": \"$eventName\",  \"eventData\": \"$eventData\" }"
    }

    /**
     * Converts the event entity to an EventRemoteEntity object.
     *
     * @return The EventRemoteEntity object.
     */

    fun toEventMap(): MutableMap<String, Any> {
        val eventMap = mutableMapOf<String, Any>()
        eventMap[NAME] = eventName
        val eventDataWithCreatedAt = mutableMapOf<String, Any>()
        eventDataWithCreatedAt.appendMap(eventData)
        eventDataWithCreatedAt[CREATED_AT] = DataFunctionHelper.formatTimeInMillis(createdAt)
        eventMap[DETAILS] = eventDataWithCreatedAt
        return eventMap
    }
}
