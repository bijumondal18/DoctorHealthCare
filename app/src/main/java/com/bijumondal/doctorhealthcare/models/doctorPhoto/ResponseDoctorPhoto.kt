package com.bijumondal.doctorhealthcare.models.doctorPhoto

data class ResponseDoctorPhoto(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: List<String>
)