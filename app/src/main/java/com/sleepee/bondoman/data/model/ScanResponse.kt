package com.sleepee.bondoman.data.model

data class ScanResponse (
    val title: String,
    val amount: Int,
    val location: String,
    val category: String,
    val locationLink: String
)