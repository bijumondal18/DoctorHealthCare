package com.bijumondal.doctorhealthcare.models.appointmentsListForDoctor

data class ResponseAppointmentsListForDoctor(
    val `data`: ArrayList<Data>,
    val success: Boolean,
    val message: String,
    val errors: String
)