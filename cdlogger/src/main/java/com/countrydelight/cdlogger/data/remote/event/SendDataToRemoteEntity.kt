package com.countrydelight.cdlogger.data.remote.event

import com.google.gson.annotations.SerializedName


/**
 * Represents data to be sent to a remote entity.
 *
 * @property appName The name of the app.
 * @property appVersionName The version name of app.
 * @property appVersionCode The version code of app.
 * @property userDetails A map of user details.
 * @property eventDetails The event to be sent.
 */
internal data class SendDataToRemoteEntity(
    @SerializedName("app_name")
    val appName: String,
    @SerializedName("app_version_name")
    val appVersionName: String,
    @SerializedName("app_version_code")
    val appVersionCode: Long?,
    @SerializedName("user_device_details")
    val userDetails: MutableMap<String, Any>,
    @SerializedName("event_details")
    val eventDetails: MutableMap<String, Any>,
)