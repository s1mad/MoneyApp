package com.example.moneyapp.presentation.ui.screens.main

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.moneyapp.presentation.ui.graphs.MainScreen
import com.example.moneyapp.presentation.utils.Result
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun AccountsScreen(navController: NavController, modifier: Modifier, viewModel: MoneyViewModel) {
    when (viewModel.accounts.collectAsState().value) {
        is Result.Pending -> viewModel.getUserAccounts()
        is Result.Loading -> CircularProgressIndicator()
        is Result.Error -> Toast.makeText(
            LocalContext.current,
            (viewModel.accounts.collectAsState().value as Result.Error).exception.message,
            Toast.LENGTH_SHORT
        ).show()

        is Result.Success -> {
            val accounts = (viewModel.accounts.collectAsState().value as Result.Success).data

            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .padding(vertical = 8.dp)
            ) {
                items(accounts) { account ->
                    Row(
                        modifier = Modifier
                            .height(60.dp)
                            .clickable {
                                viewModel.updateActiveAccount(account)
                                navController.navigate("${MainScreen.Accounts.route}/${account.id}")
                            }
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(text = account.name, modifier = Modifier.weight(1f))
                        Text(
                            text = "${account.balance} ₽",
                            color = if (account.balance < 0) MaterialTheme.colorScheme.error else Color.Unspecified
                        )
                    }
                }
            }
        }
    }
}
