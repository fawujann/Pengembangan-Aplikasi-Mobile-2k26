package com.example.pamp3

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.pamp3.components.ProfileCard
import com.example.pamp3.components.ProfileHeader

@Composable
fun ProfileScreen() {
    Column(modifier = Modifier.fillMaxSize()) {
        ProfileHeader(name = "M Fauzan Naufal")
        ProfileCard(
            bio = "Mahasigma PAM Kelas Pak Habib.",
            email = "pawujangskibidi67@gmail.com",
            phone = "08123456789",
            location = "Los Pollos Hermanos"
        )
    }
}