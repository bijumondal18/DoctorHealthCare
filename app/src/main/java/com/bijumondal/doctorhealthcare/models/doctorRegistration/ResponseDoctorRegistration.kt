package com.bijumondal.doctorhealthcare.models.doctorRegistration

data class ResponseDoctorRegistration(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)