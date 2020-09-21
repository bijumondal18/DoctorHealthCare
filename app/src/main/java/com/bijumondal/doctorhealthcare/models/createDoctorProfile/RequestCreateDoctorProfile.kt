package com.bijumondal.doctorhealthcare.models.createDoctorProfile

data class RequestCreateDoctorProfile(
    val address: String,
    val department: String,
    val doctor_id: String,
    val email: String,
    val photo: String,
    val mobile: String,
    val firstname: String,
    val lastname: String,
    val profile: String,
    val hospital_id: String,
    val visit_amount: String
)