package com.example.moneyapp.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.moneyapp.ui.graphs.AuthScreen
import com.example.moneyapp.ui.graphs.Graph

@Composable
fun SignUpScreen(navController: NavHostController) {
    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
        Text(text = "SignUp",
            modifier = Modifier.clickable {
                navController.popBackStack()
                navController.navigate(Graph.MAIN)
            })
        Text(
            text = "Login",
            modifier = Modifier.clickable { navController.navigate(AuthScreen.Login.route) })
    }
}