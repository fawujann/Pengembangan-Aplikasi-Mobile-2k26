package com.example.pamp2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

@Composable
fun App() {
    MaterialTheme {
        val manager = remember { NewsFeedManager() }

        // Membaca StateFlow secara reaktif di Compose
        val readCount by manager.readCount.collectAsState()

        val logs = remember { mutableStateListOf<String>() }
        var isStreaming by remember { mutableStateOf(false) }
        var timeCounter by remember { mutableStateOf(0) }

        // Coroutine Lifecycle Compose untuk menjalankan Flow
        LaunchedEffect(Unit) {
            isStreaming = true
            logs.add("Sistem: Memulai live feed data stream (Filter: Teknologi)...")

            manager.getNewsFeedStream()
                // 2. Filter berita berdasarkan kategori
                .filter { it.category.equals("Teknologi", ignoreCase = true) }
                // 3. Transform data (map)
                .map { news ->
                    timeCounter += 2
                    DisplayNews(
                        id = news.id,
                        formattedHeadline = "[${news.category.uppercase()}] ${news.title}",
                        timeOffset = "+${timeCounter}s"
                    )
                }
                // Operators: onEach
                .onEach { formatted ->
                    logs.add("Masuk (${formatted.timeOffset}): ${formatted.formattedHeadline}")
                }
                // Error handling .catch
                .catch { e ->
                    logs.add("Error via .catch: ${e.message}")
                }
                .collect { formattedItem ->
                    val origin = NewsItem(
                        id = formattedItem.id,
                        title = formattedItem.formattedHeadline,
                        category = "Teknologi",
                        content = "Ulasan mendalam mengenai industri teknologi terkini."
                    )
                    // 5. Coroutines async/await
                    val detailDeferred = manager.fetchNewsDetailAsync(origin)
                    val detailResult = detailDeferred.await()
                    logs.add(" -> $detailResult")

                    // 4. Update StateFlow
                    manager.markAsRead()
                }

            isStreaming = false
            logs.add("Sistem: Simulasi selesai.")
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(
                    text = "News Feed Simulator (PAM P2)",
                    fontSize = 20.sp,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Card StateFlow Display
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Status: ${if (isStreaming) "Streaming (2s)..." else "Selesai"}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Total Dibaca (StateFlow): $readCount",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Console Stream Log:",
                    style = MaterialTheme.typography.labelLarge
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Log output box
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF1E1E1E))
                        .padding(12.dp)
                ) {
                    items(logs) { log ->
                        Text(
                            text = log,
                            color = if (log.startsWith(" ->")) Color(0xFF4CAF50) else Color(0xFFE0E0E0),
                            fontSize = 13.sp,
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}