package com.bijumondal.doctorhealthcare.models.bookAppointment

data class RequestBookAppointment(
    val age: String,
    val app_date: String,
    val bloodgroup: String,
    val doctor_id: String,
    val name: String,
    val patient_id: String,
    val sex: String,
    val timeslot: String
)