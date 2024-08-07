package com.carnot.swaraj.eol

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.carnot.swaraj.eol.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        requestCameraPermission()

        binding.btnLinkDeviceAndTractor.setOnClickListener {
            if (checkCameraPermission()) {
                startActivity(Intent(this, LinkDeviceAndTractorActivity::class.java))
            }else {
                Toast.makeText(this, "Camera permission not provided", Toast.LENGTH_SHORT).show()
            }

        }

        binding.btnEndOfLineTesting.setOnClickListener {
            // Handle navigation for End of Line Testing
        }
    }

    private val requestCameraPermissionLauncher = this.registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission granted, initiate QR scan
        } else {
            // Permission denied, show toast
            Toast.makeText(this, "Camera permission not provided", Toast.LENGTH_SHORT).show()
        }
    }


    private fun checkCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this@MainActivity, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.CAMERA)) {
            // Show an explanation to the user why the permission is needed
            Toast.makeText(this, "Camera permission is needed to scan QR codes", Toast.LENGTH_LONG).show()
        }
        requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
    }
}
