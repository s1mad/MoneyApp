package com.example.moneyapp.presentation.ui.screens.main.components.accounts

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import com.example.moneyapp.data.source.local.roomdb.entity.Account
import com.example.moneyapp.presentation.ui.screens.main.components.ConfirmDeleteDialog
import com.example.moneyapp.presentation.ui.screens.main.components.OptionsExposedDropdownMenuBox
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel
import kotlin.math.round

@Composable
fun UpdateAccountDialog(
    navController: NavHostController,
    isUpdateAccount: MutableState<Boolean>,
    viewModel: MoneyViewModel
) {
    if (isUpdateAccount.value) {
        val account = viewModel.activeAccount.value
        val banks = viewModel.banks.collectAsState().value

        val name = remember { mutableStateOf(account.name) }
        val balance = remember { mutableStateOf(account.balance.toString()) }
        val bankId = remember { mutableStateOf(account.bankId) }

        val isSuccessfullyUpdated = remember { mutableStateOf<Boolean?>(null) }
        val showConfirmDeletedDialog = remember { mutableStateOf(false) }
        val context = LocalContext.current

        val activeAccount = remember { mutableStateOf(Account(0, null, "Error", 0.0)) }

        AlertDialog(
            title = { Text(text = "Edit Account") },
            text = {
                Column {
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { name.value = it },
                        label = { Text(text = "Name") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    OutlinedTextField(
                        value = balance.value,
                        onValueChange = { balance.value = it.replace(',', '.') },
                        label = { Text(text = "Balance") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                    OptionsExposedDropdownMenuBox(
                        options = banks.associateBy({ it.id }, { it.name }),
                        selectedOptionId = bankId,
                        label = "Bank"
                    )
                }
            },
            onDismissRequest = { isUpdateAccount.value = false },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    IconButton(onClick = { showConfirmDeletedDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete account",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    Button(onClick = { isUpdateAccount.value = false }) {
                        Text(text = "Cancel")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (balance.value.toDoubleOrNull() == null && balance.value.toDouble() > 0) {
                        Toast.makeText(
                            context,
                            "Balance is incorrectly write",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    } else if (balance.value.toDouble() > 1_000_000_000_000) {
                        Toast.makeText(context, "Balance is too big", Toast.LENGTH_LONG).show()
                    } else {
                        activeAccount.value = Account(
                            id = account.id,
                            userId = account.userId,
                            bankId = bankId.value,
                            name = name.value,
                            balance = round(balance.value.toDouble() * 100) / 100
                        )
                        viewModel.updateAccount(
                            activeAccount.value,
                            isSuccessfullyUpdated
                        )

                    }
                }) {
                    Text(text = "Update")
                }
            })

        if (isSuccessfullyUpdated.value == true) {
            Toast.makeText(context, "Account successfully updated", Toast.LENGTH_LONG).show()
            isUpdateAccount.value = false
        } else if (isSuccessfullyUpdated.value == false) {
            Toast.makeText(context, "Name already in use", Toast.LENGTH_LONG).show()
        }

        ConfirmDeleteDialog(
            showConfirmDialog = showConfirmDeletedDialog,
            delete = { viewModel.deleteAccount(account) },
            onComplete = {
                isUpdateAccount.value = false
                navController.navigateUp()
            }
        )
    }
}
