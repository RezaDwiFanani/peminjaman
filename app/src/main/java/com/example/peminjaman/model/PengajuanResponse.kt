package com.example.peminjaman.model

data class PengajuanResponse(
    val id: Int,
    val nama: String,
    val gaji: Int,
    val umur: Int,
    val pinjaman: Int,
    val skor: Double,
    val status: String,
    val keputusan: String
)
//