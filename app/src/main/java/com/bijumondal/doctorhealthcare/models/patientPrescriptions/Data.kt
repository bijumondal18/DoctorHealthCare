package com.bijumondal.doctorhealthcare.models.patientPrescriptions

data class Data(
    val advice: String,
    val age: String,
    val bloodgroup: Any,
    val department: String,
    val doctor: String,
    val doctor_id: String,
    val medicine: String,
    val name: String,
    val note: String,
    val patient_id: String,
    val phone: Any,
    val sex: String,
    val symptom: String
)