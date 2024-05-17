package com.example.moneyapp.presentation.ui.screens.main.components.accounts

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import com.example.moneyapp.data.source.local.roomdb.entity.Account
import com.example.moneyapp.presentation.ui.screens.main.components.OptionsExposedDropdownMenuBox
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel
import kotlin.math.round

@Composable
fun InsertAccountDialog(
    isInsertAccount: MutableState<Boolean>,
    viewModel: MoneyViewModel
) {
    val userId = viewModel.currentUser.value?.id
    if (isInsertAccount.value && userId != null) {
        val banks = viewModel.banks.collectAsState().value

        val name = remember { mutableStateOf("") }
        val balance = remember { mutableStateOf("") }
        val bankId = remember { mutableStateOf<Long?>(null) }

        val isSuccessfullyAdded = remember { mutableStateOf<Boolean?>(null) }
        val context = LocalContext.current

        AlertDialog(
            title = { Text(text = "Add Account") },
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
                            imeAction = ImeAction.Done
                        )
                    )
                    OptionsExposedDropdownMenuBox(
                        options = banks.associateBy({ it.id }, { it.name }),
                        selectedOptionId = bankId,
                        label = "Bank"
                    )
                }
            },
            onDismissRequest = { isInsertAccount.value = false },
            dismissButton = {
                Button(onClick = { isInsertAccount.value = false }) {
                    Text(text = "Cancel")
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
                        viewModel.insertAccount(
                            Account(
                                userId = userId,
                                name = name.value,
                                bankId = bankId.value,
                                balance = round(balance.value.toDouble() * 100) / 100
                            ),
                            isSuccessfullyAdded
                        )
                    }
                }) {
                    Text(text = "Add")
                }
            })
        if (isSuccessfullyAdded.value == true) {
            Toast.makeText(context, "Account successfully added", Toast.LENGTH_LONG).show()
            isInsertAccount.value = false
        } else if (isSuccessfullyAdded.value == false) {
            Toast.makeText(context, "Name already in use", Toast.LENGTH_LONG).show()
        }
    }
}
