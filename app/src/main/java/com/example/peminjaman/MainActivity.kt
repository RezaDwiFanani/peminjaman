package com.example.peminjaman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {

    var nama by remember { mutableStateOf("") }
    var gaji by remember { mutableStateOf("") }
    var umur by remember { mutableStateOf("") }
    var pinjaman by remember { mutableStateOf("") }

    var status by remember { mutableStateOf("") }
    var skor by remember { mutableStateOf(0.0) }
    var alasan by remember { mutableStateOf(listOf<String>()) }
    var pesan by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("💳 Smart Kredit AI") }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // 🔥 HEADER CARD
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Cek Kelayakan Pinjaman",
                        style = MaterialTheme.typography.titleLarge)
                    Text("Gunakan AI untuk analisis cepat & akurat")
                }
            }

            // 🔥 INPUT CARD
            Card {
                Column(modifier = Modifier.padding(16.dp)) {

                    OutlinedTextField(
                        value = nama,
                        onValueChange = { nama = it },
                        label = { Text("Nama") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = gaji,
                        onValueChange = { gaji = it },
                        label = { Text("Gaji") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = umur,
                        onValueChange = { umur = it },
                        label = { Text("Umur") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = pinjaman,
                        onValueChange = { pinjaman = it },
                        label = { Text("Jumlah Pinjaman") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // 🔥 BUTTON
            Button(
                onClick = {
                    if (nama.isBlank() || gaji.isBlank() || umur.isBlank() || pinjaman.isBlank()) {
                        pesan = "⚠️ Isi semua data!"
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
                        pesan = ""
                        status = ""
                        alasan = emptyList()

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
                                        alasan = result?.alasan ?: emptyList()
                                    } else {
                                        pesan = "❌ Error ${response.code()}"
                                    }
                                }

                                override fun onFailure(call: Call<PredictResponse>, t: Throwable) {
                                    loading = false
                                    pesan = "❌ ${t.message}"
                                }
                            })

                    } catch (e: Exception) {
                        pesan = "⚠️ Input harus angka"
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("🚀 Analisis Sekarang")
            }

            // LOADING
            if (loading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            // ERROR
            if (pesan.isNotEmpty()) {
                Text(pesan, color = MaterialTheme.colorScheme.error)
            }

            // 🔥 RESULT CARD
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
                    Column(modifier = Modifier.padding(16.dp)) {

                        Text(
                            text = if (status == "LAYAK") "🟢 LAYAK" else "🔴 DITOLAK",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text("Skor: ${skor.toInt()}%")

                        Spacer(modifier = Modifier.height(10.dp))

                        LinearProgressIndicator(
                            progress = skor.toFloat() / 100f,
                            modifier = Modifier.fillMaxWidth()
                        )

                        if (alasan.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Text("📌 Alasan:", style = MaterialTheme.typography.titleMedium)

                            alasan.forEach {
                                Text("• $it")
                            }
                        }
                    }
                }
            }
        }
    }
}