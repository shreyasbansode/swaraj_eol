package com.carnot.swaraj.eol

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LinkDeviceAndTractorViewModel : ViewModel() {
    val isWeb: Boolean = false  // Change this based on platform detection

    private val _isDeviceQrScanned = MutableLiveData(false)
    val isDeviceQrScanned: LiveData<Boolean> get() = _isDeviceQrScanned

    private val _isTractorVinScanned = MutableLiveData(false)
    val isTractorVinScanned: LiveData<Boolean> get() = _isTractorVinScanned

    private val _deviceImei = MutableLiveData("")
    val deviceImei: LiveData<String> get() = _deviceImei

    private val _deviceIccid = MutableLiveData("")
    val deviceIccid: LiveData<String> get() = _deviceIccid

    private val _isSubmitEnabled = MutableLiveData(false)
    val isSubmitEnabled: LiveData<Boolean> get() = _isSubmitEnabled

    fun setDeviceQr(result: String) {
        _isDeviceQrScanned.value = true
        _deviceImei.value = "89746512230"  // Replace with actual parsed IMEI
        _deviceIccid.value = "365536553565963"  // Replace with actual parsed ICCID
        checkSubmitEnabled()
    }

    fun setTractorVin(result: String) {
        _isTractorVinScanned.value = true
        checkSubmitEnabled()
    }

    private fun checkSubmitEnabled() {
        _isSubmitEnabled.value = _isDeviceQrScanned.value == true && _isTractorVinScanned.value == true
    }
}
