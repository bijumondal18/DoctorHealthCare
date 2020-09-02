package com.bijumondal.doctorhealthcare.api

import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.ImageView
import androidx.annotation.RequiresApi
import com.bijumondal.doctorhealthcare.R
import com.bijumondal.doctorhealthcare.models.patientPhoto.ResponsePatientPhoto
import com.bijumondal.doctorhealthcare.utils.HealthCarePreference
import com.bijumondal.doctorhealthcare.utils.Helper
import com.bijumondal.doctorhealthcare.utils.ImageLoader
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class APIHandler {

    companion object {
        private const val TAG = "APIHandler"
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun uploadImage(context: Context, imgView: ImageView, file: File) {
            val mFile = RequestBody.create(MediaType.parse("image/*"), file)
            val fileToUpload = MultipartBody.Part.createFormData("image", file.name, mFile)
            Helper.showLoading(context)
            val call: Call<ResponsePatientPhoto> = APIInterface.create().getPatientProfilePhoto(fileToUpload, mFile)
            Log.d(TAG, "request : $mFile")
            call.enqueue(object : Callback<ResponsePatientPhoto> {
                override fun onResponse(
                    call: Call<ResponsePatientPhoto>,
                    response: Response<ResponsePatientPhoto>
                ) {
                    Helper.hideLoading()
                    if (response.isSuccessful) {
                        Helper.showLog(TAG, "Response : ${response.body()!!}")
                        if (response.body()!!.success) {
                            val mData = response.body()!!.data
                            if (mData != null) {
                                if (mData.photo != null) {
                                    ImageLoader.loadCircleImageFromUrl(imgView, mData.photo, R.drawable.ic_avatar)
                                }

                                Helper.hideLoading()

                            } else {
                                if (response.body()!!.message != null) {
                                    Helper.toastShort(context, response.body()!!.message)

                                } else if (response.body()!!.errors != null && response.body()!!.errors[0] != null) {
                                    Helper.toastShort(context, response.body()!!.errors[0])
                                } else {
                                    Helper.toastShort(context, "Data not saved, response : Failed!")
                                }
                            }
                        } else {
                            if (response.body()!!.message != null) {
                                Helper.toastShort(context, response.body()!!.message)

                            } else if (response.body()!!.errors != null && response.body()!!.errors[0] != null) {
                                Helper.toastShort(context, response.body()!!.errors[0])
                            } else {
                                Helper.toastShort(context, "Response : Failed")
                            }

                        }

                    } else {
                        Helper.toastNetworkError(context)
                    }
                }

                override fun onFailure(call: Call<ResponsePatientPhoto>, t: Throwable) {
                    Helper.hideLoading()
                    if (t.message != null) {
                        Helper.showLog(TAG, t.message!!)
                        Helper.toastShort(context, t.message!!)
                    }

                }

            })
        }


    }
}