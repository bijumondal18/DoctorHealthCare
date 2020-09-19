package com.bijumondal.doctorhealthcare.models.addMedicine

data class RequestAddMedicine(
    val doctor_id: String,
    val generic: String,
    val hospital_id: String,
    val name: String
)