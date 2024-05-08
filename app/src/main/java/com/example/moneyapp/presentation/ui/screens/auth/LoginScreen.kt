package com.example.moneyapp.presentation.ui.screens.auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.moneyapp.presentation.ui.graphs.AuthScreen
import com.example.moneyapp.presentation.ui.graphs.Graph
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: MoneyViewModel
) {
    viewModel.currentUser.value?.run { navController.navigate(Graph.MAIN) }
    val email = rememberSaveable { mutableStateOf("") }
    val password = rememberSaveable { mutableStateOf("") }
    val errorsList = remember { mutableStateListOf<String>() }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        OutlinedTextField(
            value = email.value,
            onValueChange = { email.value = it },
            label = { Text(text = "Email") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password.value,
            onValueChange = { password.value = it },
            label = { Text(text = "Password") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                errorsList.addAll(
                    getErrorsList(
                        email = email.value,
                        password = password.value
                    )
                )
                if (errorsList.isEmpty()) {
                    viewModel.loginUser(
                        email = email.value,
                        password = password.value,
                        errorsList = errorsList
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Sign in")
        }
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate(AuthScreen.SignUp.route) },
            text = "Don't have an account yet??",
            textAlign = TextAlign.Center,
        )
    }

    if (errorsList.isNotEmpty()) {
        AlertDialog(
            title = { Text(text = "Errors") },
            text = {
                Column {
                    errorsList.forEach {
                        Text(text = it)
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    errorsList.clear()
                }) {
                    Text(text = "Continue")
                }
            },
            onDismissRequest = {
                errorsList.clear()
            },
        )
    }
}