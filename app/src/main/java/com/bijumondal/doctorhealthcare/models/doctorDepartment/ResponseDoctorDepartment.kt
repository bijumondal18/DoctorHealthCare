package com.bijumondal.doctorhealthcare.models.doctorDepartment

data class ResponseDoctorDepartment(
    val `data`: ArrayList<Data>,
    val success: Boolean,
    val message: String,
    val errors: String
)