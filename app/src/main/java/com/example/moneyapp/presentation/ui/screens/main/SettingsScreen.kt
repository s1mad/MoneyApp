package com.example.moneyapp.presentation.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moneyapp.presentation.ui.graphs.MainScreen
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun SettingsScreen(
    viewModel: MoneyViewModel,
    navController: NavHostController,
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .height(60.dp)
                .clickable {
                    navController.navigate(MainScreen.Categories.route)
                }
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Categories", modifier = Modifier.weight(1f))
            Text(text = viewModel.categories.collectAsState().value.size.toString())
        }
    }
}