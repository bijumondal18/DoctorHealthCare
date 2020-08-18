package com.bijumondal.doctorhealthcare.api

import com.bijumondal.doctorhealthcare.Constants
import com.bijumondal.doctorhealthcare.models.doctorLogin.RequestDoctorLogin
import com.bijumondal.doctorhealthcare.models.doctorLogin.ResponseDoctorLogin
import com.bijumondal.doctorhealthcare.models.doctorRegistration.RequestDoctorRegistration
import com.bijumondal.doctorhealthcare.models.doctorRegistration.ResponseDoctorRegistration
import com.bijumondal.doctorhealthcare.models.patientLogin.RequestPatientLogin
import com.bijumondal.doctorhealthcare.models.patientLogin.ResponsePatientLogin
import com.bijumondal.doctorhealthcare.models.patientRegistration.RequestPatientRegistration
import com.bijumondal.doctorhealthcare.models.patientRegistration.ResponsePatientRegistration
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface APIInterface {
    companion object Factory {
        fun create(): APIInterface {
            val retrofit = Retrofit.Builder()
                .client(provideOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(Gson()))
                .baseUrl(Constants.API_BASE_URL)
                .build()

            return retrofit.create(APIInterface::class.java)
        }

        private fun provideOkHttpClient(): OkHttpClient {
            val okHttpClientBuilder = OkHttpClient.Builder()
            okHttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS)
            okHttpClientBuilder.readTimeout(30, TimeUnit.SECONDS)
            okHttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS)
            return okHttpClientBuilder.build()
        }
    }


    @POST(Constants.PATIENT_REGISTRATION_URL)
    fun patientRegistration(@Body request: RequestPatientRegistration): Call<ResponsePatientRegistration>

    @POST(Constants.PATIENT_LOGIN_URL)
    fun patientLogin(@Body request: RequestPatientLogin): Call<ResponsePatientLogin>

    @POST(Constants.DOCTOR_REGISTRATION_URL)
    fun doctorRegistration(@Body request: RequestDoctorRegistration): Call<ResponseDoctorRegistration>

    @POST(Constants.DOCTOR_LOGIN_URL)
    fun doctorLogin(@Body request: RequestDoctorLogin): Call<ResponseDoctorLogin>


}
