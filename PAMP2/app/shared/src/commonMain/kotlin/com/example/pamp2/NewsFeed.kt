package com.example.pamp2

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

data class NewsItem(
    val id: Int,
    val title: String,
    val category: String,
    val content: String
)

data class DisplayNews(
    val id: Int,
    val formattedHeadline: String,
    val timeOffset: String
)

class NewsFeedManager(
    private val dispatcher: CoroutineDispatcher = Dispatchers.Default
) {
    // 4. StateFlow untuk menyimpan jumlah berita yang sudah dibaca
    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    private val dummyNews = listOf(
        NewsItem(1, "Kotlin Multiplatform Makin Populer di 2026", "Teknologi", "KMP terus berkembang pesat di kalangan pengembang mobile."),
        NewsItem(2, "Timnas Indonesia Raih Kemenangan Dramatis", "Olahraga", "Pertandingan sengit berakhir dengan skor 2-1."),
        NewsItem(3, "Pasar Saham Regional Menguat Pagi Ini", "Bisnis", "Indeks harga saham gabungan dibuka menghijau."),
        NewsItem(4, "Fitur Baru Coroutines Resmi Diperkenalkan", "Teknologi", "Peningkatan performa signifikan pada pemrosesan Flow stream."),
        NewsItem(5, "Tips Menjaga Kebugaran di Musim Pancaroba", "Kesehatan", "Konsumsi air putih dan olahraga teratur menjadi kunci."),
        NewsItem(6, "Startup AI Lokal Raih Pendanaan Seri A", "Teknologi", "Inovasi generative AI Indonesia menarik perhatian investor.")
    )

    // 1. Flow yang mensimulasikan data berita baru setiap 2 detik
    fun getNewsFeedStream(shouldSimulateError: Boolean = false): Flow<NewsItem> = flow {
        var index = 0
        while (index < dummyNews.size) {
            delay(2000)
            if (shouldSimulateError && index == 3) {
                throw RuntimeException("Koneksi simulator terputus!")
            }
            emit(dummyNews[index])
            index++
        }
    }.flowOn(dispatcher)

    // 5. Coroutines untuk mengambil detail berita secara async
    suspend fun fetchNewsDetailAsync(news: NewsItem): Deferred<String> = coroutineScope {
        async(dispatcher) {
            delay(800)
            "DETAIL: \"${news.content}\""
        }
    }

    fun markAsRead() {
        _readCount.value += 1
    }
}