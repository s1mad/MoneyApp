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
        startDestination = Graph.AUTH,
        route = Graph.ROOT
    ) {
        authNavigationGraph(navController = navController, viewModel = viewModel)
        composable(route = Graph.MAIN) {
            MainScreen(viewModel = viewModel)
        }
    }
    if (viewModel.currentUser.value == null) {
        navController.navigate(Graph.AUTH) {
            popUpTo(Graph.ROOT) { inclusive = true }
        }
    } else {
        navController.navigate(Graph.MAIN) {
            popUpTo(Graph.ROOT) { inclusive = true }
        }
    }
}

object Graph {
    const val ROOT = "ROOT_GRAPH"
    const val AUTH = "AUTH_GRAPH"
    const val MAIN = "MAIN_GRAPH"
    const val DETAILS = "DETAILS_GRAPH"
}