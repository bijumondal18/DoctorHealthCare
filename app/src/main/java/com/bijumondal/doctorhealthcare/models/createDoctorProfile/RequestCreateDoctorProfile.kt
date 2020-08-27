package com.bijumondal.doctorhealthcare.models.createDoctorProfile

data class RequestCreateDoctorProfile(
    val address: String,
    val department: String,
    val doctor_id: String,
    val email: String,
    val mobile: String,
    val name: String,
    val profile: String,
    val hospital_id: Int,
    val visit_amount: String
)