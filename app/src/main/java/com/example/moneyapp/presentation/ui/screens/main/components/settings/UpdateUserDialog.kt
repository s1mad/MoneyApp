package com.example.moneyapp.presentation.ui.screens.main.components.settings

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
import com.example.moneyapp.presentation.ui.screens.main.components.ConfirmDeleteDialog
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun UpdateUserDialog(
    isUpdateUser: MutableState<Boolean>,
    viewModel: MoneyViewModel
) {
    val user = viewModel.currentUser.value
    if (isUpdateUser.value && user != null) {
        val name = remember { mutableStateOf(user.name) }

        val showConfirmDeletedDialog = remember { mutableStateOf(false) }
        val isSuccessfullyUpdated = remember { mutableStateOf<Boolean?>(null) }

        AlertDialog(
            title = { Text(text = "Edit User") },
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
            onDismissRequest = { isUpdateUser.value = false },
            dismissButton = {
                Row(horizontalArrangement = Arrangement.SpaceBetween) {
                    IconButton(onClick = { showConfirmDeletedDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete user",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    Button(
                        onClick = { isUpdateUser.value = false }) {
                        Text(text = "Cancel")
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.updateUser(
                        user.copy(name = name.value),
                        isSuccessfullyUpdated
                    )
                }) {
                    Text(text = "Update")
                }
            }
        )
        if (isSuccessfullyUpdated.value == true) {
            Toast.makeText(LocalContext.current, "Name successfully updated", Toast.LENGTH_SHORT)
                .show()
            isUpdateUser.value = false
        } else if (isSuccessfullyUpdated.value == false) {
            Toast.makeText(
                LocalContext.current,
                "An unknown problem has arisen",
                Toast.LENGTH_SHORT
            ).show()
        }

        ConfirmDeleteDialog(
            showConfirmDialog = showConfirmDeletedDialog,
            delete = { result -> viewModel.deleteUser(user, result) },
            onComplete = {
                isUpdateUser.value = false
                viewModel.removeCurrentUser()
            }
        )
    }
}