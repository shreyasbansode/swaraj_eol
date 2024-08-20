package com.carnot.swaraj.eol.data

data class DeviceStatusResponse(
    val gps: Boolean,
    val gsm: Boolean,
    val battery: Boolean,
    val fuel: Boolean,
    var activation_id:Int = 0
)