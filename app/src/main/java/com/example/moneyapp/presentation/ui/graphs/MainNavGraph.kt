package com.example.moneyapp.presentation.ui.graphs

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.moneyapp.presentation.ui.screens.main.AccountDetailsScreen
import com.example.moneyapp.presentation.ui.screens.main.AccountsScreen
import com.example.moneyapp.presentation.ui.screens.main.CategoriesScreen
import com.example.moneyapp.presentation.ui.screens.main.SettingsScreen
import com.example.moneyapp.presentation.ui.screens.main.TransactionsScreen
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun MainNavigationGraph(
    navController: NavHostController,
    viewModel: MoneyViewModel,
    modifier: Modifier
) {
    NavHost(
        navController = navController,
        route = Graph.MAIN,
        startDestination = MainScreen.Accounts.route
    ) {
        composable(route = MainScreen.Accounts.route) {
            AccountsScreen(
                navController = navController,
                modifier = modifier,
                viewModel = viewModel
            )
        }
        composable(route = MainScreen.AccountDetails.route) {backStackEntry ->
            AccountDetailsScreen(
                viewModel = viewModel,
                modifier = modifier,
                accountId = backStackEntry.arguments?.getString("accountId")?.toLongOrNull() ?: -1L
            )
        }
        composable(route = MainScreen.Transactions.route) {
            TransactionsScreen(viewModel = viewModel, modifier = modifier)
        }
        composable(route = MainScreen.Settings.route) {
            SettingsScreen(viewModel = viewModel, navController = navController, modifier = modifier)
        }
        composable(route = MainScreen.Categories.route) {
            CategoriesScreen(viewModel = viewModel, modifier = modifier)
        }
    }
}

sealed class MainScreen(val route: String) {
    data object Accounts : MainScreen(route = "ACCOUNTS")
    data object AccountDetails : MainScreen(route = "ACCOUNTS/{accountId}")
    data object Transactions : MainScreen(route = "TRANSACTIONS")
    data object Settings : MainScreen(route = "SETTINGS")
    data object Categories : MainScreen(route = "CATEGORIES")
}

