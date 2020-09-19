package com.bijumondal.doctorhealthcare.models.addMedicineTemp

data class ResponseAddMedicineTemp(
    val `data`: ArrayList<Data>,
    val success: Boolean,
    val message: String,
    val errors: String
)