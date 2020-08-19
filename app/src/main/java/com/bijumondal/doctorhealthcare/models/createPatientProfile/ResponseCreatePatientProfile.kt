package com.bijumondal.doctorhealthcare.models.createPatientProfile

data class ResponseCreatePatientProfile(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)