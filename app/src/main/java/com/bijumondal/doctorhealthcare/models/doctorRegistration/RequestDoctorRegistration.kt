package com.bijumondal.doctorhealthcare.models.doctorRegistration

data class RequestDoctorRegistration(
    val email: String,
    val firstname: String,
    val lastname: String,
    val mobile: String,
    val password: String
)