package com.bijumondal.doctorhealthcare.utils

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class RuntimePermissions {

    companion object {
        var isChecked: Boolean = false
        private const val READ_REQUEST_CODE = 101
        private const val WRITE_REQUEST_CODE = 102
        private const val CAMERA_REQUEST_CODE = 103
        private const val CALL_REQUEST_CODE = 104
        private const val SMS_REQUEST_CODE = 105
        private const val LOCATION_REQUEST_CODE = 106
        private const val CONTACT_REQUEST_CODE = 107

        fun checkReadPermission(context: Context): Boolean {
            val permission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(
                context as AppCompatActivity,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                READ_REQUEST_CODE
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "Permission to Read denied")
                return false
            }
            return true
        }

        fun checkWritePermission(context: Context): Boolean {
            val permission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            ActivityCompat.requestPermissions(
                context as AppCompatActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_REQUEST_CODE
            )
            if (permission != PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "Permission to Write denied")
                return false
            }
            return true
        }

        fun checkCameraPermission(context: Context): Boolean {
            val permission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            )
            ActivityCompat.requestPermissions(
                context as AppCompatActivity,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "Permission to Read & Write denied")
                return false
            }
            return true
        }

        fun checkContactPermission(context: Context): Boolean {
            val permission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_CONTACTS
            )
            ActivityCompat.requestPermissions(
                context as AppCompatActivity,
                arrayOf(Manifest.permission.READ_CONTACTS),
                CONTACT_REQUEST_CODE
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "Permission to read contact denied")
                return false
            }
            return true
        }

        fun checkCallPermission(context: Context): Boolean {
            val permission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            )
            ActivityCompat.requestPermissions(
                context as AppCompatActivity,
                arrayOf(Manifest.permission.CALL_PHONE),
                CALL_REQUEST_CODE
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "Permission to call phone denied")
                return false
            }
            return true
        }

        fun checkSMSPermission(context: Context): Boolean {
            val permission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_SMS
            )
            ActivityCompat.requestPermissions(
                context as AppCompatActivity,
                arrayOf(Manifest.permission.READ_SMS),
                SMS_REQUEST_CODE
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "Permission to SMS denied")
                return false
            }
            return true
        }

        fun checkLocationPermission(context: Context): Boolean {
            val permission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            ActivityCompat.requestPermissions(
                context as AppCompatActivity,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                LOCATION_REQUEST_CODE
            )

            if (permission != PackageManager.PERMISSION_GRANTED) {
                Log.i("TAG", "Permission to Location denied")
                return false
            }
            return true
        }


    }
}