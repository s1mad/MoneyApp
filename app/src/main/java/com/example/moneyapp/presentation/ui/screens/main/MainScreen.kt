package com.example.moneyapp.presentation.ui.screens.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.moneyapp.presentation.ui.graphs.Graph
import com.example.moneyapp.presentation.ui.graphs.MainNavigationGraph
import com.example.moneyapp.presentation.ui.screens.main.components.BottomBar
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController(),
    viewModel: MoneyViewModel
) {
    Scaffold(
        bottomBar = { BottomBar(navController = navController) }
    ) { innerPadding ->
        val modifier = Modifier.padding(innerPadding)
        MainNavigationGraph(
            navController = navController,
            viewModel = viewModel,
            modifier = modifier
        )
    }
}
