package com.countrydelight.cdlogger.data.remote.event

import com.google.gson.annotations.SerializedName


/**
 * A data class representing a space request model.
 *
 * @property text The text to be processed.
 */
internal data class SpaceRequestModel(
    @SerializedName("text") var text: String? = null
)