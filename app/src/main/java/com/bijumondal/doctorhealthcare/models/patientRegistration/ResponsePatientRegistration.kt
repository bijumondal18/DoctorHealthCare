package com.bijumondal.doctorhealthcare.models.patientRegistration

data class ResponsePatientRegistration(
    val `data`: Data,
    val result: String,
    val message: String,
    val errors: String
)