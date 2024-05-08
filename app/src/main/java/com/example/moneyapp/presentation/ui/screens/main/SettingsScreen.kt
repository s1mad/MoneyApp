package com.example.moneyapp.presentation.ui.screens.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun SettingsScreen(viewModel: MoneyViewModel, modifier: Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(text = viewModel.currentUser.value?.name ?: "Unknown")
        Button(onClick = { viewModel.removeCurrentUser() }) {
            Text(text = "Log out")
        }
    }
}