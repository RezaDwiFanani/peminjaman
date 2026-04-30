package com.example.peminjaman.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.peminjaman.model.PredictRequest
import com.example.peminjaman.model.PredictResponse
import com.example.peminjaman.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    var nama by remember { mutableStateOf("") }
    var gaji by remember { mutableStateOf("") }
    var umur by remember { mutableStateOf("") }
    var pinjaman by remember { mutableStateOf("") }

    var status by remember { mutableStateOf("") }
    var skor by remember { mutableStateOf(0.0) }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💳 Smart Kredit") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {

            // 🔥 HEADER MODERN
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Cek Kelayakan Pinjaman",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = "Analisis cepat dengan AI",
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }

            // 🔥 INPUT SECTION
            Card {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    OutlinedTextField(
                        value = nama,
                        onValueChange = { nama = it },
                        label = { Text("👤 Nama") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = gaji,
                        onValueChange = { gaji = it },
                        label = { Text("💰 Gaji") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = umur,
                        onValueChange = { umur = it },
                        label = { Text("🎂 Umur") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = pinjaman,
                        onValueChange = { pinjaman = it },
                        label = { Text("🏦 Pinjaman") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 🔥 BUTTON BESAR
            Button(
                onClick = {

                    if (nama.isBlank() || gaji.isBlank() || umur.isBlank() || pinjaman.isBlank()) {
                        error = "⚠️ Isi semua data!"
                        return@Button
                    }

                    try {
                        val request = PredictRequest(
                            nama,
                            gaji.toInt(),
                            umur.toInt(),
                            pinjaman.toInt()
                        )

                        loading = true
                        error = ""
                        status = ""

                        RetrofitClient.instance.predict(request)
                            .enqueue(object : Callback<PredictResponse> {

                                override fun onResponse(
                                    call: Call<PredictResponse>,
                                    response: Response<PredictResponse>
                                ) {
                                    loading = false

                                    if (response.isSuccessful) {
                                        val result = response.body()
                                        status = result?.status ?: "-"
                                        skor = result?.skor ?: 0.0
                                    } else {
                                        error = "❌ Error ${response.code()}"
                                    }
                                }

                                override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                                    loading = false
                                    error = "❌ ${t.message}"
                                }
                            })

                    } catch (e: Exception) {
                        error = "⚠️ Input harus angka"
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp)
            ) {
                Text("🚀 Analisis Sekarang")
            }

            // LOADING
            if (loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            // ERROR
            if (error.isNotEmpty()) {
                Text(error, color = MaterialTheme.colorScheme.error)
            }

            // 🔥 RESULT CARD (LEVEL UP)
            if (status.isNotEmpty()) {

                val warna =
                    if (status == "LAYAK")
                        MaterialTheme.colorScheme.primaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer

                Card(
                    colors = CardDefaults.cardColors(containerColor = warna),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {

                        Text(
                            text = if (status == "LAYAK") "🟢 LAYAK" else "🔴 DITOLAK",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Skor Kelayakan: ${skor.toInt()}%")

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = skor.toFloat() / 100f,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }

}