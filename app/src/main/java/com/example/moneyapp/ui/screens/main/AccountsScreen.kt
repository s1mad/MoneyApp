package com.example.moneyapp.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.moneyapp.ui.graphs.Graph

@Composable
fun AccountsScreen(navController: NavController, modifier: Modifier) {
    Column {
        Text(text = "AccountsScreen")
        Text(text = "AccountsScreen", modifier = Modifier.clickable { navController.navigate(Graph.DETAILS) })
    }
}