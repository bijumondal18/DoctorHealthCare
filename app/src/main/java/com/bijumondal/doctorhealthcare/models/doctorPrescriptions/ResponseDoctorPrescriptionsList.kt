package com.bijumondal.doctorhealthcare.models.doctorPrescriptions

data class ResponseDoctorPrescriptionsList(
    val `data`: ArrayList<Data>,
    val success: Boolean,
    val message: String,
    val errors: String
)