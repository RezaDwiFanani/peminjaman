package com.example.peminjaman

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.peminjaman.model.PengajuanResponse
import com.example.peminjaman.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                com.example.peminjaman.ui.HistoryScreen()
            }
        }
    }
}

@Composable
fun HistoryScreen() {

    var data by remember { mutableStateOf(listOf<PengajuanResponse>()) }
    var loading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        RetrofitClient.instance.getPengajuan()
            .enqueue(object : Callback<List<PengajuanResponse>> {
                override fun onResponse(
                    call: Call<List<PengajuanResponse>>,
                    response: Response<List<PengajuanResponse>>
                ) {
                    loading = false
                    if (response.isSuccessful) {
                        data = response.body() ?: emptyList()
                    }
                }

                override fun onFailure(call: Call<List<PengajuanResponse>>, t: Throwable) {
                    loading = false
                }
            })
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "📊 Riwayat Pengajuan",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        } else if (data.isEmpty()) {

            // 🔥 EMPTY STATE
            Text("Belum ada data pengajuan")

        } else {

            LazyColumn {
                items(data) { item ->

                    val warna =
                        if (item.status == "LAYAK")
                            MaterialTheme.colorScheme.primaryContainer
                        else
                            MaterialTheme.colorScheme.errorContainer

                    Card(
                        colors = CardDefaults.cardColors(containerColor = warna),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {

                            Text(
                                text = "👤 ${item.nama}",
                                style = MaterialTheme.typography.titleMedium
                            )

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("Status AI: ${item.status}")
                            Text("Keputusan: ${item.keputusan}")

                            Spacer(modifier = Modifier.height(6.dp))

                            Text("Skor: ${item.skor.toInt()}%")

                            Spacer(modifier = Modifier.height(6.dp))

                            // 🔥 PROGRESS BAR
                            LinearProgressIndicator(
                                progress = item.skor.toFloat() / 100f,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}
