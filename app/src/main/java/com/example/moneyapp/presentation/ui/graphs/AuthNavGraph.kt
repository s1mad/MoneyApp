package com.example.moneyapp.presentation.ui.graphs

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.moneyapp.presentation.ui.screens.auth.LoginScreen
import com.example.moneyapp.presentation.ui.screens.auth.SignUpScreen
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

fun NavGraphBuilder.authNavigationGraph(
    navController: NavHostController,
    viewModel: MoneyViewModel
) {
    navigation(
        route = Graph.AUTH,
        startDestination = AuthScreen.Login.route
    ) {
        composable(route = AuthScreen.Login.route) {
            LoginScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = AuthScreen.SignUp.route) {
            SignUpScreen(navController = navController, viewModel = viewModel)
        }
    }
}

sealed class AuthScreen(val route: String) {
    data object Login : AuthScreen(route = "LOGIN")
    data object SignUp : AuthScreen(route = "SIGN_UP")
}
