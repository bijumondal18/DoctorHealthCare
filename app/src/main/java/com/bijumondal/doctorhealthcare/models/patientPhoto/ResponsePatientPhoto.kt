package com.bijumondal.doctorhealthcare.models.patientPhoto

data class ResponsePatientPhoto(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: List<String>
)