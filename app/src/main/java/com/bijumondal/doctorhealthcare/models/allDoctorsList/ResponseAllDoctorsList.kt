package com.bijumondal.doctorhealthcare.models.allDoctorsList

data class ResponseAllDoctorsList(
    val `data`: ArrayList<Data>,
    val success: Boolean,
    val message: String,
    val errors: String
)