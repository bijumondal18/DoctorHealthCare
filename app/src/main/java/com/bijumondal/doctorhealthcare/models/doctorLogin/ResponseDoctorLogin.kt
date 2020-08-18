package com.bijumondal.doctorhealthcare.models.doctorLogin

data class ResponseDoctorLogin(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)