package com.countrydelight.cdlogger.domain.models

internal data class DeviceDetails(
    val device: String,
    val model: String,
    val brand: String,
    val manufacturer: String,
    val sdkInt: Int
)