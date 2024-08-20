package com.carnot.swaraj.eol

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.carnot.swaraj.eol.databinding.ActivityEndOfLineTestingBinding
import com.carnot.swaraj.eol.network.ApiResponse
import com.google.zxing.integration.android.IntentIntegrator

class EndOfLineTestingActivity : AppCompatActivity() {

    private lateinit var  viewModel: EndOfLineTestingViewModel

    /*Intent Handles For QR Code Scanning*/
    var scanIntent: IntentIntegrator? = null

    private lateinit var loadingDialog: LoadingDialog
    private lateinit var customDialog: CustomDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityEndOfLineTestingBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_end_of_line_testing)

        viewModel = ViewModelProvider(this).get(EndOfLineTestingViewModel::class.java)


        // Initialize the loading dialog
        loadingDialog = LoadingDialog(this)

        // Initialize the custom dialog
        customDialog = CustomDialog(this)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        scanIntent = IntentIntegrator(this)

        binding.btnScanTractorVin.setOnClickListener {
                qrCodeScan(1)
        }

        /*viewModel.apiResponseStatus.observe(this) { response ->

            when(response){
                is ApiResponse.Loading->{
                    loadingDialog.show("Submitting...")
                }
                is ApiResponse.Error -> {
                    // Dismiss loading dialog
                    loadingDialog.dismiss()

                    // Show error message
                    Toast.makeText(this, "Error: ${response.message}", Toast.LENGTH_SHORT).show()

                }
                is ApiResponse.Success -> {
                    loadingDialog.dismiss()

                }
            }
        }*/

        viewModel.apiResponseSubmit.observe(this) { response ->

            when(response){
                is ApiResponse.Loading->{
                    loadingDialog.show("Submitting...")
                }
                is ApiResponse.Error -> {
                    // Dismiss loading dialog
                    loadingDialog.dismiss()

                    // Show error message
                    Toast.makeText(this, "Error: ${response.message}", Toast.LENGTH_SHORT).show()

                }
                is ApiResponse.Success -> {
                    loadingDialog.dismiss()
                    customDialog.show("End Of Line Testing Passed")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val result = data?.getStringExtra("SCAN_RESULT") ?: ""
            when (requestCode) {
                1 -> viewModel.scanVin(result)
            }
        }
    }

    private fun qrCodeScan(requestCode: Int) {
        scanIntent?.setOrientationLocked(true)
        scanIntent?.setPrompt("Scan a barcode or QR Code")
        scanIntent?.setRequestCode(requestCode)
        scanIntent?.initiateScan()
    }

}