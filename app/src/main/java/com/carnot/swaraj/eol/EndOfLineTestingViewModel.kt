package com.carnot.swaraj.eol

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carnot.swaraj.eol.data.BatteryVoltage
import com.carnot.swaraj.eol.data.CreateDeviceRequest
import com.carnot.swaraj.eol.data.CreateDeviceResponse
import com.carnot.swaraj.eol.data.DeviceStatusRequest
import com.carnot.swaraj.eol.data.DeviceStatusResponse
import com.carnot.swaraj.eol.data.GpsLockStatus
import com.carnot.swaraj.eol.data.GsmStatus
import com.carnot.swaraj.eol.data.PostInstallationTestRequest
import com.carnot.swaraj.eol.data.PostInstallationTestResponse
import com.carnot.swaraj.eol.network.ApiResponse
import com.carnot.swaraj.eol.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.DurationUnit

class EndOfLineTestingViewModel : ViewModel() {

    private val apiService: ApiService = ApiService.create()

    private val _isVinScanned = MutableLiveData(false)
    val isVinScanned: LiveData<Boolean> get() = _isVinScanned

    private val _gpsLockStatus = MutableLiveData(false)
    val gpsLockStatus: LiveData<Boolean> get() = _gpsLockStatus

    private val _gsmPingStatus = MutableLiveData(false)
    val gsmPingStatus: LiveData<Boolean> get() = _gsmPingStatus

    private val _batteryChargingStatus = MutableLiveData(false)
    val batteryChargingStatus: LiveData<Boolean> get() = _batteryChargingStatus

    private val _isSubmitEnabled = MutableLiveData(false)
    val isSubmitEnabled: LiveData<Boolean> get() = _isSubmitEnabled

    private val _retryTime = MutableLiveData<String>("0")
    val retryTime: LiveData<String> get() = _retryTime

    private val _vin = MutableLiveData("")
    val vin: LiveData<String> get() = _vin

    val apiResponseStatus: LiveData<ApiResponse<DeviceStatusResponse?>> get() = _apiResponseStatus
    private val _apiResponseStatus  = MutableLiveData<ApiResponse<DeviceStatusResponse?>>()

    val apiResponseSubmit: LiveData<ApiResponse<PostInstallationTestResponse?>> get() = _apiResponseSubmit
    private val _apiResponseSubmit  = MutableLiveData<ApiResponse<PostInstallationTestResponse?>>()

    var activationId = 0;

    fun scanVin(result: String) {
        // Simulate scanning VIN
        _isVinScanned.value = true
        _vin.value = result
        // Start verifying connectivity
        //verifyConnectivity()
        onVinScanned()
        startRetryTimer()
    }
    var timeLeft = 300
    private fun startRetryTimer() {
        viewModelScope.launch {
             // Retry after 5 minutes
            timeLeft = 300
            while (timeLeft > 0) {
                val minutes = timeLeft / 60
                val seconds = timeLeft % 60
                _retryTime.value = String.format("%02d:%02d", minutes, seconds)
                delay(1000)
                timeLeft -= 1
            }
            _retryTime.value = "0"
            _isSubmitEnabled.value = true
            // Retry connectivity check after timer ends
        }
    }

    fun submit() {
        viewModelScope.launch{
            _apiResponseSubmit.value = ApiResponse.Loading
            val response =  apiService.postInstallationTest(PostInstallationTestRequest( vin = _vin.value.toString(), activation_id = activationId, gsm_status = GsmStatus(status =_gpsLockStatus.value!!), gps_lock_status = GpsLockStatus(status =_gpsLockStatus.value!!), battery_voltage = BatteryVoltage(status =_batteryChargingStatus.value!!), latitude = null, longitude = null))
            if (response.status){
                _apiResponseSubmit.value = ApiResponse.Success(response.data)
            }else{
                _apiResponseSubmit.value = ApiResponse.Error("")
            }
        }
    }

    fun onRetry(){
        startRetryTimer()
        onVinScanned()
    }

    private fun onVinScanned(){
        viewModelScope.launch{
            _apiResponseStatus.value = ApiResponse.Loading
            val response =  apiService.getDeviceStatus(DeviceStatusRequest( vin = _vin.value.toString()))
            if (response.status){
                _apiResponseStatus.value = ApiResponse.Success(response.data)
                _gpsLockStatus.value = response.data!!.gps
                _gsmPingStatus.value = response.data!!.gsm
                _batteryChargingStatus.value = response.data!!.battery
                activationId = response.data!!.activation_id

                delay(10000)
                _gpsLockStatus.value = true
                delay(10000)
                _gsmPingStatus.value = true
                delay(10000)
                _batteryChargingStatus.value = true

                if (_gpsLockStatus.value == true &&
                    _gsmPingStatus.value == true &&
                    _batteryChargingStatus.value == true){
                    _isSubmitEnabled.value =  true
                }else{
                    if(_retryTime.value != "0"){
                        delay(2000)
                        onVinScanned()
                    }
                }
            }else{
                delay(2000)
                _isVinScanned.value = false
                _vin.value = ""
                _retryTime.value = "0"
                timeLeft = 0
                _apiResponseStatus.value = ApiResponse.Error(response.message)
            }
        }
    }
}