package com.bijumondal.doctorhealthcare.models.patientLogin

data class ResponsePatientLogin(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)