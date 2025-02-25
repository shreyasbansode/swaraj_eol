package com.carnot.swaraj.eol

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.carnot.swaraj.eol.data.CreateDeviceRequest
import com.carnot.swaraj.eol.data.CreateDeviceResponse
import com.carnot.swaraj.eol.network.ApiResponse
import com.carnot.swaraj.eol.network.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LinkDeviceAndTractorViewModel : ViewModel() {

    private val apiService: ApiService = ApiService.create()

    private val _isDeviceQrScanned = MutableLiveData(false)
    val isDeviceQrScanned: LiveData<Boolean> get() = _isDeviceQrScanned

    private val _isTractorVinScanned = MutableLiveData(false)
    val isTractorVinScanned: LiveData<Boolean> get() = _isTractorVinScanned

    private val _vin = MutableLiveData("")
    val vin: LiveData<String> get() = _vin

    private val _deviceImei = MutableLiveData("")
    val deviceImei: LiveData<String> get() = _deviceImei

    private val _deviceIccid = MutableLiveData("")
    val deviceIccid: LiveData<String> get() = _deviceIccid

    private val _isSubmitEnabled = MutableLiveData(false)
    val isSubmitEnabled: LiveData<Boolean> get() = _isSubmitEnabled

   val apiResponse: LiveData<ApiResponse<CreateDeviceResponse?>> get() = _apiResponse
    private val _apiResponse  = MutableLiveData<ApiResponse<CreateDeviceResponse?>>()


    fun setDeviceQr(result: String) {
        try {
            if (result.split(" ").size > 2){

                val imei = result.split(" ")[0]
                val iccid = result.split(" ")[1]

                if (imei.length != 15) {
                    viewModelScope.launch {
                        delay(100)
                        _apiResponse.value = ApiResponse.Error("Wrong QR, Please provide Correct QR Code")
                    }

                    return;
                }

                if (iccid.length < 18 || iccid.length > 20) {
                    viewModelScope.launch {
                        delay(100)
                        _apiResponse.value = ApiResponse.Error("Wrong QR, Please provide Correct QR Code")
                    }
                    return;
                }
                _isDeviceQrScanned.value = true
                _deviceImei.value = imei
                _deviceIccid.value = iccid
                checkSubmitEnabled()
            }else{
                viewModelScope.launch {
                    delay(100)
                    _apiResponse.value = ApiResponse.Error("Wrong QR, Please provide Correct QR Code")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setTractorVin(result: String) {
        _vin.value = result
        _isTractorVinScanned.value = true
        checkSubmitEnabled()
    }

    private fun checkSubmitEnabled() {
        _isSubmitEnabled.value = _isDeviceQrScanned.value == true && _isTractorVinScanned.value == true
    }

    fun onSubmitClick(){
        viewModelScope.launch{
            _apiResponse.value = ApiResponse.Loading
           val response =  apiService.createDevice(CreateDeviceRequest(imei = _deviceImei.value.toString(), iccid = _deviceIccid.value.toString(), vin = _vin.value.toString(), tractor_model_id = null))
            if (response.status){
                _apiResponse.value = ApiResponse.Success(response.data)
            }else{
                _apiResponse.value = ApiResponse.Error(response.message)
            }
        }
    }
}
