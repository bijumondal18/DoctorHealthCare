package com.bijumondal.doctorhealthcare.models.addDoctorPrescriptions

data class RequestAddPrescriptions(
    val advice: String,
    val doctor_id: String,
    val medicine: String,
    val note: String,
    val patient_id: String,
    val symptom: String
)