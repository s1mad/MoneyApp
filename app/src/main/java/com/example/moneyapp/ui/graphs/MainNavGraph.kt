package com.example.moneyapp.ui.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moneyapp.ui.screens.main.AccountDetailsScreen
import com.example.moneyapp.ui.screens.main.AccountsScreen
import com.example.moneyapp.ui.screens.main.SettingsScreen
import com.example.moneyapp.ui.screens.main.TransactionsScreen
import com.example.moneyapp.ui.screens.main.components.BottomBarScreen

@Composable
fun MainNavigationGraph(navController: NavHostController, modifier: Modifier) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = BottomBarScreen.Accounts.route
    ) {
        composable(route = BottomBarScreen.Accounts.route) {
            AccountsScreen(navController = navController, modifier = modifier)
        }
        composable(route = BottomBarScreen.Transactions.route) {
            TransactionsScreen(modifier = modifier)
        }
        composable(route = BottomBarScreen.Settings.route) {
            SettingsScreen(modifier = modifier)
        }
        composable(route = Graph.DETAILS) {
            AccountDetailsScreen(modifier = modifier)
        }
    }
}
