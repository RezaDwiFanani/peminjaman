package com.example.peminjaman.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun FilterBar(selected: String, onChange: (String) -> Unit) {

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

        listOf("ALL", "LAYAK", "DITOLAK").forEach { item ->

            FilterChip(
                selected = selected == item,
                onClick = { onChange(item) },
                label = { Text(item) }
            )
        }
    }
}
