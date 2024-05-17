package com.example.moneyapp.presentation.ui.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moneyapp.presentation.ui.screens.main.MainScreen
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun RootNavigationGraph(navController: NavHostController, viewModel: MoneyViewModel) {
    NavHost(
        navController = navController,
        startDestination = if (viewModel.currentUser.value == null) Graph.AUTH else Graph.MAIN,
        route = Graph.ROOT
    ) {
        authNavigationGraph(navController = navController, viewModel = viewModel)
        composable(route = Graph.MAIN) {
            MainScreen(viewModel = viewModel)
        }
    }
}

object Graph {
    const val ROOT = "ROOT_GRAPH"
    const val AUTH = "AUTH_GRAPH"
    const val MAIN = "MAIN_GRAPH"
}