package com.bijumondal.doctorhealthcare.models.addDoctorHolidays

data class ResponseAddDoctorHolidays(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)