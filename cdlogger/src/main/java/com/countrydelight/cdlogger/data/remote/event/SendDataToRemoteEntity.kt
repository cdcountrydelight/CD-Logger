package com.countrydelight.cdlogger.data.remote.event

import com.google.gson.annotations.SerializedName


/**
 * Represents data to be sent to a remote entity.
 *
 * @property app The name of the app.
 * @property userDetails A map of user details.
 * @property eventDetails The event to be sent.
 */
internal data class SendDataToRemoteEntity(
    val app: String,
    @SerializedName("user_details")
    val userDetails: MutableMap<String, Any>,
    @SerializedName("event_details")
    val eventDetails: MutableMap<String, Any>,
)