package com.example.moneyapp.presentation.ui.screens.main.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moneyapp.presentation.ui.graphs.MainScreen
import com.example.moneyapp.presentation.ui.screens.main.components.transactions.InsertTransactionDialog
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun FloatingButton(viewModel: MoneyViewModel, navController: NavHostController) {
    val isInsertTransaction = remember { mutableStateOf(false) }
    val isAccountDetails = remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    when (currentDestination?.route) {
        MainScreen.Transactions.route -> {
            FloatingActionButton(
                onClick = {
                    isAccountDetails.value = false
                    isInsertTransaction.value = true
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add transaction")
            }
        }

        MainScreen.AccountDetails.route -> {
            FloatingActionButton(
                onClick = {
                    isAccountDetails.value = true
                    isInsertTransaction.value = true
                }
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add transaction")
            }
        }
    }


    InsertTransactionDialog(
        isInsertTransaction = isInsertTransaction,
        iniAccountId = if (isAccountDetails.value) viewModel.activeAccount.value.id else null,
        insertTransaction = { transaction, isSuccessfullyAdded ->
            viewModel.insertTransaction(
                transaction,
                isSuccessfullyAdded
            )
        },
        insertTransactionCategory = { transactionCategory, isSuccessfullyAdded ->
            viewModel.insertTransactionCategory(
                transactionCategory,
                isSuccessfullyAdded
            )
        },
        updateAccount = { account, result ->
            viewModel.updateAccount(
                account,
                result
            )
        },
        accounts = viewModel.accounts.collectAsState().value,
        categories = viewModel.categories.collectAsState().value,
    )
}


