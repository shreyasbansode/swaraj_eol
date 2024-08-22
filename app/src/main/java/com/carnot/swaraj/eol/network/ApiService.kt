package com.carnot.swaraj.eol.network

import com.carnot.swaraj.eol.data.BaseResponse
import com.carnot.swaraj.eol.data.CreateDeviceRequest
import com.carnot.swaraj.eol.data.CreateDeviceResponse
import com.carnot.swaraj.eol.data.DeviceStatusRequest
import com.carnot.swaraj.eol.data.DeviceStatusResponse
import com.carnot.swaraj.eol.data.PostInstallationTestRequest
import com.carnot.swaraj.eol.data.PostInstallationTestResponse
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit


interface ApiService {

    @POST("/amk/sd_device_creation/")
    suspend fun createDevice(
        @Body request: CreateDeviceRequest
    ): BaseResponse<CreateDeviceResponse>


    @POST("/amk/sd_device_installation_status/")
    suspend fun getDeviceStatus(
        @Body request: DeviceStatusRequest
    ): BaseResponse<DeviceStatusResponse>

    @POST("/amk/sd_device_post_installation_test/")
    suspend fun postInstallationTest(
        @Body request: PostInstallationTestRequest
    ): BaseResponse<PostInstallationTestResponse>

    companion object {
        fun create(): ApiService {
            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES) // Set connection timeout to 10 seconds
                .readTimeout(5, TimeUnit.MINUTES) // Set read timeout to 20 seconds
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl("https://app.uat.swarajtm.in/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}