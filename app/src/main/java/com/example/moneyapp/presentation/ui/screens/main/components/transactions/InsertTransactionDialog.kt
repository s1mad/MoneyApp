@file:JvmName("InsertTransactionDialogKt")

package com.example.moneyapp.presentation.ui.screens.main.components.transactions

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.example.moneyapp.data.source.local.roomdb.entity.Account
import com.example.moneyapp.data.source.local.roomdb.entity.Category
import com.example.moneyapp.data.source.local.roomdb.entity.Operation
import com.example.moneyapp.data.source.local.roomdb.entity.Transaction
import com.example.moneyapp.data.source.local.roomdb.entity.TransactionCategory
import com.example.moneyapp.presentation.ui.screens.main.components.OptionsExposedDropdownMenuBox
import com.example.moneyapp.presentation.utils.Result
import java.util.Calendar
import kotlin.math.round

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertTransactionDialog(
    isInsertTransaction: MutableState<Boolean>,
    iniAccountId: Long? = null,
    insertTransaction: (transaction: Transaction, transactionId: MutableState<Long?>) -> Unit,
    insertTransactionCategory: (transactionCategory: TransactionCategory, isSuccessfullyAdded: MutableState<Boolean?>) -> Unit,
    updateAccount: (account: Account, result: MutableState<Result<Account>>) -> Unit,
    accounts: List<Account>,
    categories: List<Category>
) {
    if (isInsertTransaction.value && accounts.isEmpty()) {
        Toast.makeText(LocalContext.current, "First create accounts", Toast.LENGTH_LONG).show()
        isInsertTransaction.value = false
    } else if (isInsertTransaction.value) {
        val operation = remember { mutableStateOf(Operation.EXPENSE) }
        val date = rememberDatePickerState(
            initialSelectedDateMillis = Calendar.getInstance().timeInMillis
        )
        val cost = remember { mutableStateOf("") }
        val accountId = remember { mutableStateOf<Long?>(iniAccountId?: accounts[0].id) }
        val categoryId = remember { mutableStateOf<Long?>(null) }
        val place = remember { mutableStateOf("") }
        val comment = remember { mutableStateOf("") }

        val transactionId = remember { mutableStateOf<Long?>(null) }
        val isTransactionCategoryAdded = remember { mutableStateOf<Boolean?>(null) }
        val isAccountUpdated = remember { mutableStateOf<Boolean?>(null) }
        val showDatePicker = remember { mutableStateOf(false) }
        val context = LocalContext.current

        AlertDialog(
            title = { Text(text = "Add Transaction") },
            text = {
                Column {
                    Row {
                        Button(
                            modifier = Modifier.weight(1f),
                            onClick = {
                                operation.value =
                                    if (operation.value == Operation.EXPENSE) Operation.INCOME
                                    else Operation.EXPENSE
                            }) {
                            Text(text = operation.value.toString())
                        }

                        IconButton(
                            modifier = Modifier.wrapContentSize(),
                            onClick = { showDatePicker.value = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Date picker"
                            )
                        }
                    }
                    OutlinedTextField(
                        value = cost.value,
                        onValueChange = { cost.value = it },
                        label = { Text(text = "Cost") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Next
                        )
                    )
                    OptionsExposedDropdownMenuBox(
                        options = accounts.associateBy({ it.id }, { it.name }),
                        selectedOptionId = accountId,
                        label = "Account",
                        isNullableOption = false
                    )
                    OptionsExposedDropdownMenuBox(
                        options = categories.associateBy({ it.id }, { it.name }),
                        selectedOptionId = categoryId,
                        label = "Category"
                    )
                    OutlinedTextField(
                        value = place.value,
                        onValueChange = { place.value = it },
                        label = { Text(text = "Place") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next
                        )
                    )
                    OutlinedTextField(
                        value = comment.value,
                        onValueChange = { comment.value = it },
                        label = { Text(text = "Comment") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done
                        )
                    )
                }
            },
            onDismissRequest = { isInsertTransaction.value = false },
            dismissButton = {
                Button(onClick = { isInsertTransaction.value = false }) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (cost.value.toDoubleOrNull() == null || cost.value.toDouble() <= 0) {
                        Toast.makeText(
                            context,
                            "Cost is incorrectly write",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    } else if (cost.value.toDouble() > 1_000_000_000_000) {
                        Toast.makeText(context, "Cost is too big", Toast.LENGTH_LONG).show()
                    } else if ((date.selectedDateMillis
                            ?: Long.MAX_VALUE) > Calendar.getInstance().timeInMillis
                    ) {
                        Toast.makeText(
                            context,
                            "Selected date cannot be in the future",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        insertTransaction(
                            Transaction(
                                accountId = accountId.value!!,
                                operation = operation.value,
                                cost = round(cost.value.toDouble() * 100) / 100,
                                place = place.value,
                                comment = comment.value,
                                date = date.selectedDateMillis
                                    ?: Calendar.getInstance().timeInMillis
                            ),
                            transactionId
                        )
                    }
                }) {
                    Text(text = "Add")
                }
            })

        if (showDatePicker.value) {
            DatePickerDialog(
                onDismissRequest = { showDatePicker.value = false },
                confirmButton = {
                    Button(onClick = { showDatePicker.value = false }) {
                        Text(text = "Choose")
                    }
                }) {
                DatePicker(state = date)
            }
        }

        if (transactionId.value != null) {
            if (transactionId.value != -1L) {
                Toast.makeText(context, "Transaction successfully added", Toast.LENGTH_LONG)
                    .show()
                accounts.find { it.id == accountId.value }?.let {account ->
                    updateAccount(
                        account.copy(
                            balance = account.balance + Transaction.getCostOperation(
                                operation.value,
                                round(cost.value.toDouble() * 100) / 100
                            )
                        ), isAccountUpdated
                    )
                }

                if (categoryId.value != null) {
                    insertTransactionCategory(
                        TransactionCategory(
                            transactionId = transactionId.value!!,
                            categoryId = categoryId.value!!
                        ),
                        isTransactionCategoryAdded
                    )
                }

                if (isAccountUpdated.value == true && isTransactionCategoryAdded.value == true) {
                    isInsertTransaction.value = false
                } else if (isAccountUpdated.value == false) {
                    Toast.makeText(
                        context,
                        "Balance has not been updated",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (isTransactionCategoryAdded.value == false) {
                    Toast.makeText(
                        context,
                        "The category has not been added",
                        Toast.LENGTH_LONG
                    ).show()
                } else if (transactionId.value == -1L) {
                    Toast.makeText(
                        context,
                        "An unknown problem has arisen",
                        Toast.LENGTH_LONG
                    ).show()
                }
                isInsertTransaction.value = false
            }
        }
    }
}
