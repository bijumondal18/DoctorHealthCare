package com.bijumondal.doctorhealthcare.models.bookAppointment

data class ResponseBookAppointment(
    val `data`: Data,
    val success: Boolean,
    val message: String
)