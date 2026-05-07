package com.example.peminjaman.network

import com.example.peminjaman.model.PredictRequest
import com.example.peminjaman.model.PredictResponse
import com.example.peminjaman.model.PengajuanResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("predict")
    fun predict(@Body request: PredictRequest): Call<PredictResponse>


    @GET("pengajuan")
    fun getPengajuan(): Call<List<PengajuanResponse>>
}
