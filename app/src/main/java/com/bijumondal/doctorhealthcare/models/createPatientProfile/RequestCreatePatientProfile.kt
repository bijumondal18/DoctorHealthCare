package com.bijumondal.doctorhealthcare.models.createPatientProfile

data class RequestCreatePatientProfile(
    val address: String,
    val photo: String,
    val age: String,
    val bloodgroup: String,
    val dob: String,
    val email: String,
    val mobile: String,
    val firstname: String,
    val lastname: String,
    val patient_id: String,
    val sex: String
)