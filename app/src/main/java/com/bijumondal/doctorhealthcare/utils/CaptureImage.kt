package com.bijumondal.doctorhealthcare.utils

import android.content.Context
import android.content.Intent
import android.provider.MediaStore
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class CaptureImage {
    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2

        fun showPictureDialog(context: Context) {
            val pictureDialog = AlertDialog.Builder(context)
            pictureDialog.setTitle("Select Image")
            val pictureDialogItems = arrayOf("from Gallery", "from Camera")
            pictureDialog.setItems(
                pictureDialogItems
            ) { _, which ->
                when (which) {
                    0 -> choosePhotoFromGallery(context)
                    1 -> takePhotoFromCamera(context)
                }
            }
            pictureDialog.show()
        }

        private fun choosePhotoFromGallery(context: Context) {
            if (RuntimePermissions.checkWritePermission(context)
                && RuntimePermissions.checkReadPermission(context)
            ) {
                val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                )
                (context as AppCompatActivity).startActivityForResult(galleryIntent, GALLERY)
            }
        }

        private fun takePhotoFromCamera(context: Context) {
            if (RuntimePermissions.checkCameraPermission(context)
                && RuntimePermissions.checkWritePermission(context)
                && RuntimePermissions.checkReadPermission(context)
            ) {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

                (context as AppCompatActivity).startActivityForResult(cameraIntent, CAMERA)
            }
        }
    }

}