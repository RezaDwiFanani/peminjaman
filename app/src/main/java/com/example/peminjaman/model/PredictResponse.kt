package com.example.peminjaman.model

data class PredictResponse(
    val status: String,
    val skor: Double,
    val alasan: List<String>
)

