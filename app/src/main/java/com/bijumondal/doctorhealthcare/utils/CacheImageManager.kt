package com.bijumondal.doctorhealthcare.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.bijumondal.doctorhealthcare.models.banners.Data
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception

class CacheImageManager {
    companion object {

        fun getImage(context: Context, bannerItem: Data): Bitmap {
            val fileName: String = "${context.cacheDir}/${bannerItem.bannername}"
            val file: File = File(fileName)
            var bitmap: Bitmap? = null
            try {
                bitmap = BitmapFactory.decodeStream(FileInputStream(file))

            } catch (e: Exception) {
                e.printStackTrace()
            }

            return bitmap!!
        }

        fun putImage(context: Context, bannerItem: Data, bitmap: Bitmap) {
            val fileName: String = "${context.cacheDir}/${bannerItem.bannername}"
            val file: File = File(fileName)
            var outputStream: FileOutputStream? = null

            try {
                outputStream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close()

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        }

    }

}
