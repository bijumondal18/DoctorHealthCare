package com.bijumondal.doctorhealthcare.models.addMedicine

data class ResponseAddMedicine(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)