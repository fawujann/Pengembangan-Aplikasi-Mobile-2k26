package com.example.pamp2

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform