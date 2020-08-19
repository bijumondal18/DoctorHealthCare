package com.bijumondal.doctorhealthcare.models.createDoctorProfile

data class ResponseCreateDoctorProfile(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)