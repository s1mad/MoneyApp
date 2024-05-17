package com.example.moneyapp.presentation.ui.screens.main.components.categories

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import com.example.moneyapp.data.source.local.roomdb.entity.Category
import com.example.moneyapp.presentation.ui.screens.main.components.ConfirmDeleteDialog
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun UpdateCategoryDialog(
    category: MutableState<Category?>,
    viewModel: MoneyViewModel
    ) {
    category.value?.let { activeCategory ->
        val name = remember { mutableStateOf(activeCategory.name) }

        val isSuccessfullyAdded = remember { mutableStateOf<Boolean?>(null) }
        val showConfirmDeletedDialog = remember { mutableStateOf(false) }

        AlertDialog(
            title = { Text(text = "Edit Category") },
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
            onDismissRequest = { category.value = null },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    IconButton(onClick = { showConfirmDeletedDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete category",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    Button(
                        onClick = { category.value = null }) {
                        Text(text = "Cancel")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.updateCategory(
                        activeCategory.copy(name = name.value),
                        isSuccessfullyAdded
                    )
                }) {
                    Text(text = "Update")
                }
            }
        )
        if (isSuccessfullyAdded.value == true) {
            Toast.makeText(
                LocalContext.current,
                "Category successfully updated",
                Toast.LENGTH_SHORT
            )
                .show()
            category.value = null
        } else if (isSuccessfullyAdded.value == false) {
            Toast.makeText(LocalContext.current, "Name already in use", Toast.LENGTH_SHORT).show()
        }
        ConfirmDeleteDialog(
            showConfirmDialog = showConfirmDeletedDialog,
            delete = { viewModel.deleteCategory(activeCategory) }
        )
    }
}