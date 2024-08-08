package com.carnot.swaraj.eol

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.carnot.swaraj.eol.databinding.ActivityLinkDeviceAndTractorBinding
import com.google.zxing.integration.android.IntentIntegrator


class LinkDeviceAndTractorActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLinkDeviceAndTractorBinding
    private lateinit var viewModel: LinkDeviceAndTractorViewModel

    /*Intent Handles For QR Code Scanning*/
    var scanIntent: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LinkDeviceAndTractorViewModel::class.java)
        binding = ActivityLinkDeviceAndTractorBinding.inflate(layoutInflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        setContentView(binding.root)

        scanIntent = IntentIntegrator(this)

        binding.btnScanDeviceQr.setOnClickListener {
                qrCodeScan(1)
        }

        binding.btnScanTractorVin.setOnClickListener {
                qrCodeScan(2)
        }

        binding.btnSubmit.setOnClickListener {
            // Handle submit action
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            val result = data?.getStringExtra("SCAN_RESULT") ?: ""
            when (requestCode) {
                1 -> viewModel.setDeviceQr(result)
                2 -> viewModel.setTractorVin(result)
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
