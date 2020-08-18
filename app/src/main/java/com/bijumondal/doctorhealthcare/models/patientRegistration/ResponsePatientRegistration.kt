package com.bijumondal.doctorhealthcare.models.patientRegistration

data class ResponsePatientRegistration(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)