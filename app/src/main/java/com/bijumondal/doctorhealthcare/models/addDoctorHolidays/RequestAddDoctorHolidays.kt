package com.bijumondal.doctorhealthcare.models.addDoctorHolidays

data class RequestAddDoctorHolidays(
    val date: String,
    val doctor_id: String,
    val hospital_id: String
)