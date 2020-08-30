package com.bijumondal.doctorhealthcare.models.doctorHolidaysList

data class ResponseDoctorHolidaysList(
    val `data`: ArrayList<Data>,
    val success: Boolean,
    val message: String,
    val errors: String

)