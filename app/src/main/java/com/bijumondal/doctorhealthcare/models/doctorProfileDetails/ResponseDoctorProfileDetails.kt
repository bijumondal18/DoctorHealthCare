package com.bijumondal.doctorhealthcare.models.doctorProfileDetails

data class ResponseDoctorProfileDetails(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)