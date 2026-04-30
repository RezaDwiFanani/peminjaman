package com.example.peminjaman.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.peminjaman.model.PengajuanResponse

@Composable
fun PengajuanCard(item: PengajuanResponse) {

    val warna =
        if (item.status == "LAYAK")
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.errorContainer

    Card(
        colors = CardDefaults.cardColors(containerColor = warna),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("👤 ${item.nama}", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(6.dp))

            Text("Status: ${item.status}")
            Text("Keputusan: ${item.keputusan}")
            Text("Skor: ${item.skor.toInt()}%")

            Spacer(modifier = Modifier.height(8.dp))

            LinearProgressIndicator(
                progress = item.skor.toFloat() / 100f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}