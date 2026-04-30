package com.example.peminjaman.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen() {

    Column(modifier = Modifier.padding(16.dp)) {

        Text("ℹ️ Tentang Aplikasi", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            """
Aplikasi ini adalah Sistem Pendukung Keputusan berbasis AI 
untuk menilai kelayakan peminjaman.

Fitur:
- Analisis kelayakan (AI)
- Riwayat pengajuan
- Statistik data

Dibuat untuk keperluan skripsi.
            """.trimIndent()
        )
    }
}