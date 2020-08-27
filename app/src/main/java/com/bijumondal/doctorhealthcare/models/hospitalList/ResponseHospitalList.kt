package com.bijumondal.doctorhealthcare.models.hospitalList

data class ResponseHospitalList(
    val `data`: List<Data>,
    val success: Boolean,
    val message: String,
    val errors: String
)