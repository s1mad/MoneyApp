package com.example.moneyapp.presentation.ui.screens.main

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun AccountDetailsScreen(modifier: Modifier, viewModel: MoneyViewModel, accountId: Long) {
    TransactionsScreen(
        modifier = modifier.fillMaxSize(),
        viewModel = viewModel,
        accountId = accountId
    )
}