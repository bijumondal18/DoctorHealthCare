package com.bijumondal.doctorhealthcare.models.patientPrescriptions

data class ResponsePatientPrescriptionsList(
    val `data`: List<Data>,
    val success: Boolean
)