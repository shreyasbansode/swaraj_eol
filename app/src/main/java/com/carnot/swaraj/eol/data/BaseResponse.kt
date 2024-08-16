package com.carnot.swaraj.eol.data

data class BaseResponse<T>(
    val status: Boolean,
    val message: String,
    val data: T?
)