package com.carnot.swaraj.eol.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(private val apiService: ApiService) {

    suspend fun fetchData(): ApiResponse<Any> {
        return withContext(Dispatchers.IO) {
            apiService.fetchData()
        }
    }
}