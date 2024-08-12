package com.carnot.swaraj.eol

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EndOfLineTestingViewModel : ViewModel() {

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

    private val _retryTime = MutableLiveData<Long>(0)
    val retryTime: LiveData<Long> get() = _retryTime

    private val _vin = MutableLiveData("")
    val vin: LiveData<String> get() = _vin

    fun scanVin(result: String) {
        // Simulate scanning VIN
        _isVinScanned.value = true
        _vin.value = result
        // Start verifying connectivity
        verifyConnectivity()
    }

    private fun verifyConnectivity() {
        viewModelScope.launch {
            delay(5000) // Simulate a delay for verification
            // Randomly set statuses for demonstration
            _gpsLockStatus.value = true
            _gsmPingStatus.value = false
            _batteryChargingStatus.value = true
            _isSubmitEnabled.value = _gpsLockStatus.value == true &&
                    _gsmPingStatus.value == true &&
                    _batteryChargingStatus.value == true
            if (_isSubmitEnabled.value == false) {
                startRetryTimer()
            }
        }
    }

    private fun startRetryTimer() {
        viewModelScope.launch {
            var timeLeft = 300 // Retry after 5 minutes
            while (timeLeft > 0) {
                _retryTime.value = timeLeft.toLong()
                delay(1000)
                timeLeft -= 1
            }
            _retryTime.value = 0
            // Retry connectivity check after timer ends
            verifyConnectivity()
        }
    }

    fun submit() {
        // Handle submission
    }
}