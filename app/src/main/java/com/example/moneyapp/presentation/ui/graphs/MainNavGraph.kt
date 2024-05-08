package com.example.moneyapp.presentation.ui.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moneyapp.presentation.ui.screens.main.AccountDetailsScreen
import com.example.moneyapp.presentation.ui.screens.main.AccountsScreen
import com.example.moneyapp.presentation.ui.screens.main.SettingsScreen
import com.example.moneyapp.presentation.ui.screens.main.TransactionsScreen
import com.example.moneyapp.presentation.ui.screens.main.components.BottomBarScreen
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun MainNavigationGraph(navController: NavHostController, viewModel: MoneyViewModel, modifier: Modifier) {
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
            SettingsScreen(viewModel = viewModel, modifier = modifier)
        }
        composable(route = Graph.DETAILS) {
            AccountDetailsScreen(modifier = modifier)
        }
    }
}
