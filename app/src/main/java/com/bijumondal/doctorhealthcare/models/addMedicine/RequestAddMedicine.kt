package com.bijumondal.doctorhealthcare.models.addMedicine

data class RequestAddMedicine(
    val date: String,
    val doctor_id: String,
    val generic: String,
    val hospital_id: String,
    val instructions: String,
    val name: String,
    val patient_id: String
)