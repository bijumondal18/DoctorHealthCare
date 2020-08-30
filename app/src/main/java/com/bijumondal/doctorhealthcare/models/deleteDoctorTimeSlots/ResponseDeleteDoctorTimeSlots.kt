package com.bijumondal.doctorhealthcare.models.deleteDoctorTimeSlots

data class ResponseDeleteDoctorTimeSlots(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)