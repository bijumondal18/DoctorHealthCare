package com.bijumondal.doctorhealthcare.models.patientProfileDetails

data class ResponsePatientProfileDetails(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)