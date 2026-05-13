package com.example.peminjaman.ui

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

@Composable
fun HistoryScreen() {

    var data by remember { mutableStateOf(listOf<PengajuanResponse>()) }
    var loading by remember { mutableStateOf(true) }
    var filter by remember { mutableStateOf("ALL") }

    fun loadData() {
        loading = true

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

    LaunchedEffect(Unit) {
        loadData()
    }

    val filteredData = when (filter) {
        "LAYAK" -> data.filter { it.status == "LAYAK" }
        "DITOLAK" -> data.filter { it.status == "DITOLAK" }
        else -> data
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            text = "📊 Riwayat Pengajuan",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(12.dp))

        FilterBar(filter) { filter = it }

        Spacer(modifier = Modifier.height(12.dp))

        if (loading) {
            CircularProgressIndicator()
        } else if (filteredData.isEmpty()) {
            Text("Belum ada data")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {

                items(filteredData) { item ->
                    PengajuanCard(item)
                }
            }
        }
    }
}
