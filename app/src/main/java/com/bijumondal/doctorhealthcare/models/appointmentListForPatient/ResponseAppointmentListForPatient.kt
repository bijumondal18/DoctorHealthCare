package com.bijumondal.doctorhealthcare.models.appointmentListForPatient

data class ResponseAppointmentListForPatient(
    val `data`: ArrayList<Data>,
    val success: Boolean,
    val message: String,
    val errors: String
)