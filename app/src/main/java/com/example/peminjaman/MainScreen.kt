package com.example.peminjaman

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.peminjaman.ui.AboutScreen
import com.example.peminjaman.ui.HistoryScreen
import com.example.peminjaman.ui.HomeScreen
import com.example.peminjaman.ui.StatsScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    var selected by remember { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {

                NavigationBarItem(
                    selected = selected == 0,
                    onClick = { selected = 0 },
                    label = { Text("Beranda") },
                    icon = { Text("🏠") }
                )

                NavigationBarItem(
                    selected = selected == 1,
                    onClick = { selected = 1 },
                    label = { Text("Riwayat") },
                    icon = { Text("📊") }
                )

                NavigationBarItem(
                    selected = selected == 2,
                    onClick = { selected = 2 },
                    label = { Text("Statistik") },
                    icon = { Text("📈") }
                )

                NavigationBarItem(
                    selected = selected == 3,
                    onClick = { selected = 3 },
                    label = { Text("Tentang") },
                    icon = { Text("ℹ️") }
                )
            }
        }
    ) { padding ->

        when (selected) {
            0 -> HomeScreen()
            1 -> HistoryScreen()
            2 -> StatsScreen()
            3 -> AboutScreen()
        }
    }
}
