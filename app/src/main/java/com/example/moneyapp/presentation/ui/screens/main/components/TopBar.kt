package com.example.moneyapp.presentation.ui.screens.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.moneyapp.presentation.ui.graphs.MainScreen
import com.example.moneyapp.presentation.ui.screens.main.components.accounts.InsertAccountDialog
import com.example.moneyapp.presentation.ui.screens.main.components.accounts.UpdateAccountDialog
import com.example.moneyapp.presentation.ui.screens.main.components.categories.InsertCategoryDialog
import com.example.moneyapp.presentation.ui.screens.main.components.settings.UpdateUserDialog
import com.example.moneyapp.presentation.ui.screens.main.components.transactions.InsertTransactionDialog
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController, viewModel: MoneyViewModel) {
    val isInsertAccount = remember { mutableStateOf(false) }
    val isUpdateAccount = remember { mutableStateOf(false) }
    val isInsertTransaction = remember { mutableStateOf(false) }
    val isUpdateUser = remember { mutableStateOf(false) }
    val isInsertCategory = remember { mutableStateOf(false) }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val totalBalance = viewModel.accounts.collectAsState().value.sumOf { it.balance }.toLong()

    TopAppBar(
        title = {
            when (currentDestination?.route) {
                MainScreen.Accounts.route, MainScreen.Transactions.route -> {
                    Column(verticalArrangement = Arrangement.Center) {
                        Text(
                            text = "$totalBalance ₽",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(text = "Balance", fontSize = 14.sp, color = Color.Gray)
                    }
                }

                MainScreen.AccountDetails.route -> {
                    Column {
                        Text(
                            text = "${viewModel.activeAccount.value.balance} ₽",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = viewModel.activeAccount.value.name,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                MainScreen.Settings.route -> {
                    Text(
                        text = viewModel.currentUser.value?.name ?: "Unknown",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                MainScreen.Categories.route -> {
                    Text(
                        text = "Categories: ${viewModel.categories.collectAsState().value.size}",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        actions = {
            when (currentDestination?.route) {
                MainScreen.Accounts.route -> {
                    IconButton(onClick = { viewModel.getUserData() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                    IconButton(onClick = { isInsertAccount.value = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Account")
                    }
                }

                MainScreen.AccountDetails.route -> {
                    IconButton(onClick = { isUpdateAccount.value = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Account")
                    }
                }

                MainScreen.Transactions.route -> {
                    IconButton(onClick = { viewModel.getUserData() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }

                MainScreen.Settings.route -> {
                    IconButton(onClick = { isUpdateUser.value = true }) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit user")
                    }
                    IconButton(onClick = { viewModel.removeCurrentUser() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Get out")
                    }
                }

                MainScreen.Categories.route -> {
                    IconButton(onClick = { isInsertCategory.value = true }) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add Category")
                    }
                }
            }
        },
        navigationIcon = {
            when (currentDestination?.route) {
                MainScreen.AccountDetails.route -> {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }

                MainScreen.Categories.route -> {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
        )
    )


    InsertAccountDialog(
        isInsertAccount = isInsertAccount,
        viewModel = viewModel
    )
    UpdateAccountDialog(
        navController = navController,
        isUpdateAccount = isUpdateAccount,
        viewModel = viewModel
    )
    InsertTransactionDialog(
        isInsertTransaction = isInsertTransaction,
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
        updateAccount = { account, isSuccessfullyUpdated ->
            viewModel.updateAccount(
                account,
                isSuccessfullyUpdated
            )
        },
        accounts = viewModel.accounts.collectAsState().value,
        categories = viewModel.categories.collectAsState().value,
    )
    UpdateUserDialog(
        isUpdateUser = isUpdateUser,
        viewModel = viewModel
    )
    InsertCategoryDialog(
        isInsertCategory = isInsertCategory,
        viewModel = viewModel
    )
}