package com.bijumondal.doctorhealthcare.models.addDoctorTimeslots

data class ResponseAddDoctorTimeslots(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)