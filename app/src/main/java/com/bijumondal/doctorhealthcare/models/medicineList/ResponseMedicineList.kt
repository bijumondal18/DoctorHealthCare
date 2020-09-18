package com.bijumondal.doctorhealthcare.models.medicineList

data class ResponseMedicineList(
    val `data`: ArrayList<Data>,
    val success: Boolean,
    val message: String,
    val errors: String
)