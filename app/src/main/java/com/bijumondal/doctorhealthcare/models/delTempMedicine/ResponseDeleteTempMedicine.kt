package com.bijumondal.doctorhealthcare.models.delTempMedicine

data class ResponseDeleteTempMedicine(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)