package com.bijumondal.doctorhealthcare.models.banners

data class ResponseBannersList(
    val `data`: Data,
    val success: Boolean,
    val message: String,
    val errors: String
)