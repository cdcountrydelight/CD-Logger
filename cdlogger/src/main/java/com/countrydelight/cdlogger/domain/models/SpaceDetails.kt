package com.countrydelight.cdlogger.domain.models


/**
 * Represents the details of a space.
 *
 * @property spaceId The ID of the space.
 * @property spaceKey The key of the space.
 * @property spaceToken The token of the space.
 */
data class SpaceDetails(val spaceId: String, val spaceKey: String, val spaceToken: String)