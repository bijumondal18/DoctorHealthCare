package com.bijumondal.doctorhealthcare.models.patientRegistration

data class RequestPatientRegistration(
    val email: String,
    val firstname: String,
    val lastname: String,
    val mobile: String,
    val password: String
)