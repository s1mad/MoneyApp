package com.example.moneyapp.presentation.ui.screens.main.components.transactions

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.moneyapp.data.source.local.roomdb.relation.TransactionAndCategory
import com.example.moneyapp.presentation.ui.screens.main.components.ConfirmDeleteDialog
import com.example.moneyapp.presentation.ui.screens.main.components.OptionsExposedDropdownMenuBox
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel
import java.util.Calendar
import kotlin.math.round
import com.example.moneyapp.presentation.utils.Result

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateTransactionDialog(
    transaction: MutableState<TransactionAndCategory?>,
    accounts: List<Account>,
    categories: List<Category>,
    viewModel: MoneyViewModel
) {
    transaction.value?.let { activeTransaction ->
        val operation = remember { mutableStateOf(activeTransaction.transaction.operation) }
        val date = rememberDatePickerState(
            initialSelectedDateMillis = activeTransaction.transaction.date
        )
        val cost = remember { mutableStateOf(activeTransaction.transaction.cost.toString()) }
        val accountId = remember { mutableStateOf<Long?>(activeTransaction.transaction.accountId) }
        val categoryId = remember { mutableStateOf<Long?>(activeTransaction.category?.id) }
        val place = remember { mutableStateOf(activeTransaction.transaction.place) }
        val comment = remember { mutableStateOf(activeTransaction.transaction.comment) }

        val isTransactionUpdated = remember { mutableStateOf<Boolean?>(null) }
        val resultOfAccountUpdated = remember { mutableStateOf<Result<Account>>(Result.Pending) }
        val showDatePicker = remember { mutableStateOf(false) }
        val showConfirmDeletedDialog = remember { mutableStateOf(false) }
        val context = LocalContext.current

        AlertDialog(
            title = { Text(text = "Edit Transaction") },
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
            onDismissRequest = { transaction.value = null },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    IconButton(onClick = { showConfirmDeletedDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete transaction",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    Button(
                        onClick = { transaction.value = null }) {
                        Text(text = "Cancel")
                    }
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
                    } else {
                        viewModel.updateTransaction(
                            Transaction(
                                accountId = accountId.value!!,
                                operation = operation.value,
                                cost = round(cost.value.toDouble() * 100) / 100,
                                place = place.value,
                                comment = comment.value,
                                date = date.selectedDateMillis
                                    ?: Calendar.getInstance().timeInMillis,
                                id = activeTransaction.transaction.id
                            ),
                            isTransactionUpdated
                        )
                    }
                }) {
                    Text(text = "Update")
                }
            })
        if (isTransactionUpdated.value == true) {
            Toast.makeText(
                context,
                "Transaction successfully updated",
                Toast.LENGTH_LONG
            ).show()
            val oldCost = Transaction.getCostOperation(
                activeTransaction.transaction.operation,
                activeTransaction.transaction.cost
            )
            val newCost = Transaction.getCostOperation(
                operation.value,
                round(cost.value.toDouble() * 100) / 100
            )
            accounts.find { it.id == accountId.value }?.let { account ->
                viewModel.updateAccount(
                    account.copy(
                        balance = account.balance - oldCost + newCost
                    ), resultOfAccountUpdated
                )
            }
            categories.find { it.id == categoryId.value }.let { category ->
                if (activeTransaction.category != category) {
                    if (category == null && activeTransaction.category != null) {
                        viewModel.deleteTransactionCategory(
                            TransactionCategory(
                                transactionId = activeTransaction.transaction.id,
                                categoryId = activeTransaction.category.id
                            )
                        )
                    } else if (category != null && activeTransaction.category == null) {
                        viewModel.insertTransactionCategory(
                            TransactionCategory(
                                transactionId = activeTransaction.transaction.id,
                                categoryId = category.id
                            )
                        )
                    } else {
                        viewModel.updateTransactionCategory(
                            TransactionCategory(
                                transactionId = activeTransaction.transaction.id,
                                categoryId = activeTransaction.category!!.id
                            ),
                            TransactionCategory(
                                transactionId = activeTransaction.transaction.id,
                                categoryId = category!!.id
                            )
                        )
                    }
                }
            }
            if (resultOfAccountUpdated.value == true) {
                transaction.value = null
            } else if (resultOfAccountUpdated.value == false) {
                Toast.makeText(
                    context,
                    "Balance has not been updated",
                    Toast.LENGTH_LONG
                ).show()
            }
        } else if (isTransactionUpdated.value == false) {
            Toast.makeText(
                context,
                "An unknown problem has arisen",
                Toast.LENGTH_LONG
            ).show()
        }

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
        ConfirmDeleteDialog(
            showConfirmDialog = showConfirmDeletedDialog,
            delete = { result ->
                accounts.find { it.id == accountId.value }?.let { account ->
                    viewModel.updateAccount(
                        account.copy(balance = account.balance - activeTransaction.transaction.getCostOperation()),
                    )
                }
                viewModel.deleteTransaction(activeTransaction.transaction, result)
            },
            onComplete = { transaction.value = null }
        )
    }
}