package com.bijumondal.doctorhealthcare.models.addDoctorPrescriptions

data class ResponseAddPrescriptions(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)