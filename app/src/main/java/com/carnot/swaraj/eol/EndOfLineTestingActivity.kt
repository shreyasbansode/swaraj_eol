package com.carnot.swaraj.eol

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.carnot.swaraj.eol.databinding.ActivityEndOfLineTestingBinding
import com.google.zxing.integration.android.IntentIntegrator

class EndOfLineTestingActivity : AppCompatActivity() {

    private lateinit var  viewModel: EndOfLineTestingViewModel

    /*Intent Handles For QR Code Scanning*/
    var scanIntent: IntentIntegrator? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityEndOfLineTestingBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_end_of_line_testing)

        viewModel = ViewModelProvider(this).get(EndOfLineTestingViewModel::class.java)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        scanIntent = IntentIntegrator(this)

        binding.btnScanTractorVin.setOnClickListener {
                qrCodeScan(1)
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

    private fun showCustomDialog(message: String) {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.dialog_success, null)

        // Find the views in the custom dialog layout
        val closeButton: ImageView = view.findViewById(R.id.closeButton)
        val dialogMessage: TextView = view.findViewById(R.id.dialogMessage)

        // Set the custom message
        dialogMessage.text = message

        // Set a click listener for the close button
        closeButton.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        // Set the custom view for the dialog
        dialog.setContentView(view)
        dialog.setCancelable(false) // Make the dialog non-cancellable
        dialog.show()
    }
}