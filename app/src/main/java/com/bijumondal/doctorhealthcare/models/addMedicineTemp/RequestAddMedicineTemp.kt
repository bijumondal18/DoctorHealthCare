package com.bijumondal.doctorhealthcare.models.addMedicineTemp

data class RequestAddMedicineTemp(
    val date: String,
    val doctor_id: String,
    val duration: String,
    val frequency: String,
    val instructions: String,
    val medicine: String,
    val patient_id: String
)