package com.bijumondal.doctorhealthcare.models.patientPrescriptions

data class ResponsePatientPrescriptionsList(
    val `data`: ArrayList<Data>,
    val success: Boolean,
    val message: String,
    val errors: String
)