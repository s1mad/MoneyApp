package com.example.moneyapp.ui.graphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moneyapp.ui.screens.main.MainScreen

@Composable
fun RootNavigationGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Graph.AUTH,
        route = Graph.ROOT
    ) {
        authNavigationGraph(navController)
        composable(route = Graph.MAIN) {
            MainScreen()
        }
    }
}

object Graph {
    const val ROOT = "ROOT_GRAPH"
    const val AUTH = "AUTH_GRAPH"
    const val MAIN = "MAIN_GRAPH"
    const val DETAILS = "DETAILS_GRAPH"
}