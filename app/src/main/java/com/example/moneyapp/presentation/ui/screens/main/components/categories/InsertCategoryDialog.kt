package com.example.moneyapp.presentation.ui.screens.main.components.categories

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import com.example.moneyapp.data.source.local.roomdb.entity.Category
import com.example.moneyapp.presentation.utils.Result
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun InsertCategoryDialog(
    isInsertCategory: MutableState<Boolean>,
    viewModel: MoneyViewModel
) {
    val userId = viewModel.currentUser.value?.id
    if (isInsertCategory.value && userId != null) {
        val name = remember { mutableStateOf("") }

        val resultOfInsert = remember { mutableStateOf<Result<Category>>(Result.Pending) }

        AlertDialog(
            title = {
                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Add Category"
                    )
                    if (resultOfInsert.value is Result.Loading) CircularProgressIndicator()
                }
            },
            text = {
                OutlinedTextField(
                    value = name.value,
                    onValueChange = { name.value = it },
                    label = { Text(text = "Name") },
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done
                    )
                )
            },
            onDismissRequest = {
                if (resultOfInsert.value !is Result.Loading) isInsertCategory.value = false
            },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    Button(
                        onClick = { isInsertCategory.value = false },
                        enabled = resultOfInsert.value !is Result.Loading
                    ) {
                        Text(text = "Cancel")
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.insertCategory(
                            Category(
                                userId = userId,
                                name = name.value
                            ),
                            resultOfInsert
                        )
                    },
                    enabled = resultOfInsert.value !is Result.Loading
                ) {
                    Text(text = "Add")
                }
            }
        )
        when (resultOfInsert.value) {
            is Result.Success -> {
                Toast.makeText(
                    LocalContext.current,
                    "Category successfully added",
                    Toast.LENGTH_SHORT
                ).show()
                isInsertCategory.value = false
            }

            is Result.Error -> Toast.makeText(
                LocalContext.current,
                (resultOfInsert.value as Result.Error).exception.message,
                Toast.LENGTH_SHORT
            ).show()

            else -> {}
        }
    }
}