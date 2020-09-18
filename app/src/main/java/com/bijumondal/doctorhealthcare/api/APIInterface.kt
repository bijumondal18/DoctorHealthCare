package com.bijumondal.doctorhealthcare.api

import com.bijumondal.doctorhealthcare.Constants
import com.bijumondal.doctorhealthcare.models.addDoctorHolidays.RequestAddDoctorHolidays
import com.bijumondal.doctorhealthcare.models.addDoctorHolidays.ResponseAddDoctorHolidays
import com.bijumondal.doctorhealthcare.models.addDoctorPrescriptions.RequestAddPrescriptions
import com.bijumondal.doctorhealthcare.models.addDoctorPrescriptions.ResponseAddPrescriptions
import com.bijumondal.doctorhealthcare.models.addDoctorTimeslots.RequestAddDoctorTimeslots
import com.bijumondal.doctorhealthcare.models.addDoctorTimeslots.ResponseAddDoctorTimeslots
import com.bijumondal.doctorhealthcare.models.allDoctorsList.RequestAllDoctorsList
import com.bijumondal.doctorhealthcare.models.allDoctorsList.ResponseAllDoctorsList
import com.bijumondal.doctorhealthcare.models.appointmentListForPatient.RequestAppointmentListForPatient
import com.bijumondal.doctorhealthcare.models.appointmentListForPatient.ResponseAppointmentListForPatient
import com.bijumondal.doctorhealthcare.models.appointmentsListForDoctor.RequestAppointmentsListForDoctor
import com.bijumondal.doctorhealthcare.models.appointmentsListForDoctor.ResponseAppointmentsListForDoctor
import com.bijumondal.doctorhealthcare.models.banners.ResponseBannersList
import com.bijumondal.doctorhealthcare.models.bookAppointment.RequestBookAppointment
import com.bijumondal.doctorhealthcare.models.bookAppointment.ResponseBookAppointment
import com.bijumondal.doctorhealthcare.models.createDoctorProfile.RequestCreateDoctorProfile
import com.bijumondal.doctorhealthcare.models.createDoctorProfile.ResponseCreateDoctorProfile
import com.bijumondal.doctorhealthcare.models.createPatientProfile.RequestCreatePatientProfile
import com.bijumondal.doctorhealthcare.models.createPatientProfile.ResponseCreatePatientProfile
import com.bijumondal.doctorhealthcare.models.deleteDoctorHolidays.RequestDeleteDoctorHolidays
import com.bijumondal.doctorhealthcare.models.deleteDoctorHolidays.ResponseDeleteDoctorHolidays
import com.bijumondal.doctorhealthcare.models.deleteDoctorTimeSlots.RequestDeleteDoctorTimeSlots
import com.bijumondal.doctorhealthcare.models.deleteDoctorTimeSlots.ResponseDeleteDoctorTimeSlots
import com.bijumondal.doctorhealthcare.models.doctorDepartment.ResponseDoctorDepartment
import com.bijumondal.doctorhealthcare.models.doctorHolidaysList.RequestDoctorHolidaysList
import com.bijumondal.doctorhealthcare.models.doctorHolidaysList.ResponseDoctorHolidaysList
import com.bijumondal.doctorhealthcare.models.doctorLogin.RequestDoctorLogin
import com.bijumondal.doctorhealthcare.models.doctorLogin.ResponseDoctorLogin
import com.bijumondal.doctorhealthcare.models.doctorPhoto.RequestDoctorPhoto
import com.bijumondal.doctorhealthcare.models.doctorPhoto.ResponseDoctorPhoto
import com.bijumondal.doctorhealthcare.models.doctorPrescriptions.RequestDoctorPrescriptionsList
import com.bijumondal.doctorhealthcare.models.doctorPrescriptions.ResponseDoctorPrescriptionsList
import com.bijumondal.doctorhealthcare.models.doctorProfileDetails.RequestDoctorProfileDetails
import com.bijumondal.doctorhealthcare.models.doctorProfileDetails.ResponseDoctorProfileDetails
import com.bijumondal.doctorhealthcare.models.doctorRegistration.RequestDoctorRegistration
import com.bijumondal.doctorhealthcare.models.doctorRegistration.ResponseDoctorRegistration
import com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList.RequestDoctorTimeSlotsList
import com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList.ResponseDoctorTimeSlotsList
import com.bijumondal.doctorhealthcare.models.hospitalList.ResponseHospitalList
import com.bijumondal.doctorhealthcare.models.medicineList.ResponseMedicineList
import com.bijumondal.doctorhealthcare.models.patientLogin.RequestPatientLogin
import com.bijumondal.doctorhealthcare.models.patientLogin.ResponsePatientLogin
import com.bijumondal.doctorhealthcare.models.patientPhoto.RequestPatientPhoto
import com.bijumondal.doctorhealthcare.models.patientPhoto.ResponsePatientPhoto
import com.bijumondal.doctorhealthcare.models.patientPrescriptions.RequestPatientPrescriptionsList
import com.bijumondal.doctorhealthcare.models.patientPrescriptions.ResponsePatientPrescriptionsList
import com.bijumondal.doctorhealthcare.models.patientProfileDetails.RequestPatientProfileDetails
import com.bijumondal.doctorhealthcare.models.patientProfileDetails.ResponsePatientProfileDetails
import com.bijumondal.doctorhealthcare.models.patientRegistration.RequestPatientRegistration
import com.bijumondal.doctorhealthcare.models.patientRegistration.ResponsePatientRegistration
import com.google.gson.Gson
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
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


    /*
    *  PATIENT SECTION
    * */

    @POST(Constants.PATIENT_REGISTRATION_URL)
    fun patientRegistration(@Body request: RequestPatientRegistration): Call<ResponsePatientRegistration>

    @POST(Constants.PATIENT_LOGIN_URL)
    fun patientLogin(@Body request: RequestPatientLogin): Call<ResponsePatientLogin>

    @POST(Constants.PATIENT_CREATE_PROFILE_URL)
    fun createPatientProfile(@Body request: RequestCreatePatientProfile): Call<ResponseCreatePatientProfile>

    @POST(Constants.PATIENT_PROFILE_DETAILS_URL)
    fun getPatientProfileDetails(@Body request: RequestPatientProfileDetails): Call<ResponsePatientProfileDetails>

    @Multipart
    @POST(Constants.PATIENT_PHOTO_URL)
    fun getPatientProfilePhoto(@Part file: MultipartBody.Part, @Part("name") name: RequestBody): Call<ResponsePatientPhoto>

    @POST(Constants.BOOK_APPOINTMENT_URL)
    fun bookAppointment(@Body request: RequestBookAppointment): Call<ResponseBookAppointment>

    @POST(Constants.PATIENT_APPOINTMENTS_LIST_URL)
    fun getPatientAppointmentsList(@Body request: RequestAppointmentListForPatient): Call<ResponseAppointmentListForPatient>

    @POST(Constants.PATIENT_PRESCRIPTIONS_LIST_URL)
    fun getPatientPrescriptionsList(@Body request: RequestPatientPrescriptionsList): Call<ResponsePatientPrescriptionsList>


    /*
    * DOCTOR SECTION
    * */

    @POST(Constants.DOCTOR_REGISTRATION_URL)
    fun doctorRegistration(@Body request: RequestDoctorRegistration): Call<ResponseDoctorRegistration>

    @POST(Constants.DOCTOR_LOGIN_URL)
    fun doctorLogin(@Body request: RequestDoctorLogin): Call<ResponseDoctorLogin>

    @POST(Constants.DOCTOR_CREATE_PROFILE_URL)
    fun createDoctorProfile(@Body request: RequestCreateDoctorProfile): Call<ResponseCreateDoctorProfile>

    @GET(Constants.DOCTOR_DEPARTMENT_URL)
    fun getDoctorDepartments(): Call<ResponseDoctorDepartment>

    @POST(Constants.DOCTOR_PROFILE_DETAILS_URL)
    fun getDoctorProfileDetails(@Body request: RequestDoctorProfileDetails): Call<ResponseDoctorProfileDetails>

    @POST(Constants.DOCTOR_TIME_SLOT_LIST_URL)
    fun getDoctorTimeSlotsList(@Body request: RequestDoctorTimeSlotsList): Call<ResponseDoctorTimeSlotsList>

    @POST(Constants.DOCTOR_HOLIDAYS_LIST_URL)
    fun getDoctorHolidaysList(@Body request: RequestDoctorHolidaysList): Call<ResponseDoctorHolidaysList>

    @POST(Constants.ADD_DOCTOR_HOLIDAYS_URL)
    fun addDoctorHolidays(@Body request: RequestAddDoctorHolidays): Call<ResponseAddDoctorHolidays>

    @POST(Constants.ADD_DOCTOR_TIMESLOTS_URL)
    fun addDoctorTimeSlots(@Body request: RequestAddDoctorTimeslots): Call<ResponseAddDoctorTimeslots>

    @POST(Constants.DELETE_DOCTOR_HOLIDAYS_URL)
    fun deleteDoctorHolidays(@Body request: RequestDeleteDoctorHolidays): Call<ResponseDeleteDoctorHolidays>

    @POST(Constants.DELETE_DOCTOR_TIMESLOTS_URL)
    fun deleteDoctorTimeSlots(@Body request: RequestDeleteDoctorTimeSlots): Call<ResponseDeleteDoctorTimeSlots>

    @POST(Constants.DOCTOR_APPOINTMENTS_LIST_URL)
    fun getDoctorAppointmentsList(@Body request: RequestAppointmentsListForDoctor): Call<ResponseAppointmentsListForDoctor>

    @POST(Constants.ADD_PRESCRIPTIONS_URL)
    fun addPrescription(@Body request: RequestAddPrescriptions): Call<ResponseAddPrescriptions>

    @POST(Constants.DOCTOR_PRESCRIPTIONS_LIST_URL)
    fun getDoctorPrescriptionsLis(@Body request: RequestDoctorPrescriptionsList): Call<ResponseDoctorPrescriptionsList>

    @Multipart
    @POST(Constants.DOCTOR_PHOTO_URL)
    fun getDoctorProfilePhoto(@Part file: MultipartBody.Part, @Part("name") name: RequestBody): Call<ResponseDoctorPhoto>

    @GET(Constants.MEDICINE_LIST_URL)
    fun getMedicineList(): Call<ResponseMedicineList>


    /*
    * OTHERS SECTION
    * */

    @GET(Constants.BANNER_LIST_URL)
    fun getBanners(): Call<ResponseBannersList>

    @POST(Constants.ALL_DOCTORS_LIST_URL)
    fun getAllDoctorsList(@Body request: RequestAllDoctorsList): Call<ResponseAllDoctorsList>

    @GET(Constants.HOSPITAL_LIST_URL)
    fun getHospitalList(): Call<ResponseHospitalList>

}
