package com.example.peminjaman.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.peminjaman.model.PengajuanResponse
import com.example.peminjaman.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun StatsScreen() {

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

    val total = data.size
    val layak = data.count { it.status == "LAYAK" }
    val ditolak = data.count { it.status == "DITOLAK" }

    Column(modifier = Modifier.padding(16.dp)) {

        Text("📈 Statistik", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        if (loading) {
            CircularProgressIndicator()
        } else {

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Pengajuan: $total")
                    Text("Layak: $layak")
                    Text("Ditolak: $ditolak")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = if (total == 0) 0f else layak.toFloat() / total,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text("Rasio Kelayakan")
        }
    }
}