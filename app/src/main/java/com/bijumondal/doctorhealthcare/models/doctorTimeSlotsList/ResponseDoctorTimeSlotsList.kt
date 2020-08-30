package com.bijumondal.doctorhealthcare.models.doctorTimeSlotsList

data class ResponseDoctorTimeSlotsList(
    val `data`: List<Data>,
    val success: Boolean,
    val message: String,
    val errors: String

)