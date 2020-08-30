package com.bijumondal.doctorhealthcare.models.addDoctorTimeslots

data class RequestAddDoctorTimeslots(
    val doctor_id: String,
    val endtime: String,
    val hospital_id: String,
    val starttime: String,
    val weekday: String
)