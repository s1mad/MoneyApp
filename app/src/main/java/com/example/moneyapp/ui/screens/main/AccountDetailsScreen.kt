package com.example.moneyapp.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AccountDetailsScreen(modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Text(text = "AccountDetailsScreen")
    }
}