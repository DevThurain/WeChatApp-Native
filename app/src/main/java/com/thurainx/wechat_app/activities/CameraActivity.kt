package com.thurainx.wechat_app.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE
import com.thurainx.wechat_app.R
import com.thurainx.wechat_app.utils.EXTRA_BITMAP
import com.thurainx.wechat_app.utils.EXTRA_QR

val CAMERA_REQUEST_CODE = 1
class CameraActivity : AppCompatActivity() {
    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, CameraActivity::class.java)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        if(isCameraPermissionGiven()){
            capturePhoto()
        }else{
            requestCameraPermission()
        }

    }

    private fun capturePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE)
    }

    private fun isCameraPermissionGiven(): Boolean {
        return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestCameraPermission() {
        requestPermissions(
            arrayOf(Manifest.permission.CAMERA),
            QrScannerActivity.PERMISSION_REQUEST_CODE_CAMERA
        )
    }
    private fun IntArray.isPermissionGranted(): Boolean {
        return this.isNotEmpty() && this[0] == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            QrScannerActivity.PERMISSION_REQUEST_CODE_CAMERA -> {
                if (grantResults.isPermissionGranted())
                    capturePhoto()
                else
                    requestCameraPermission()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CAMERA_REQUEST_CODE){
            val bitmap = data?.extras?.get("data") as Bitmap
            Log.d("bitmap",bitmap.toString())
            data.putExtra(EXTRA_BITMAP, data.extras?.get("data").toString())
            setResult(RESULT_OK, data)
            finish()
        }else{
            Log.d("bitmap",resultCode.toString())

        }
    }



}