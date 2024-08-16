package com.carnot.swaraj.eol.data

data class PostInstallationTestRequest(
    val vin: String,
    val activation_id: Int,
    val gps_lock_status: Boolean,
    val gsm_status: Boolean,
    val battery_voltage: Boolean,
    val latitude: Float?,  // Nullable to make it optional
    val longitude: Float?  // Nullable to make it optional
)