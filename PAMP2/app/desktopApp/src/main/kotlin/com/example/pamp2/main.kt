package com.example.pamp2

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(onCloseRequest = ::exitApplication, title = "News Feed Simulator") {
        App()
    }
}