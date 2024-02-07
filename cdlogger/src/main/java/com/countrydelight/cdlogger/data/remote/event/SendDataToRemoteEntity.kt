package com.countrydelight.cdlogger.data.remote.event

import com.countrydelight.cdlogger.domain.models.DeviceDetails


/**
 * Represents data to be sent to a remote entity.
 *
 * @property appName The name of the app.
 * @property appUid The UID of the app.
 * @property advertisingId The advertising ID of the device.
 * @property userDetails A map of user details.
 * @property deviceDetails The details of the device.
 * @property event The event to be sent.
 */
internal data class SendDataToRemoteEntity(
    val appName: String,
    val appUid: String,
    val advertisingId: String?,
    val userDetails: MutableMap<String, Any>,
    val deviceDetails: DeviceDetails?,
    val event: EventRemoteEntity,
)