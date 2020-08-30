package com.bijumondal.doctorhealthcare.models.deleteDoctorHolidays

data class ResponseDeleteDoctorHolidays(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)