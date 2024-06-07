package com.example.moneyapp.presentation.ui.screens.main.components

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.moneyapp.presentation.utils.Result

@Composable
fun ConfirmDeleteDialog(
    showConfirmDialog: MutableState<Boolean>,
    delete: (result: MutableState<Result<String>>) -> MutableState<Result<String>>,
    onComplete: () -> Unit = {}
) {
    if (showConfirmDialog.value) {
        val resultOfDelete = remember { mutableStateOf<Result<String>>(Result.Pending) }

        AlertDialog(
            title = {
                Row {
                    Text(
                        modifier = Modifier.weight(1f),
                        text = "Confirm delete"
                    )
                    if (resultOfDelete.value is Result.Loading) CircularProgressIndicator()
                }
                Text(text = "Confirm delete")
            },
            onDismissRequest = {
                if (resultOfDelete.value !is Result.Loading) showConfirmDialog.value = false
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmDialog.value = false },
                    enabled = resultOfDelete.value !is Result.Loading
                ) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        delete(resultOfDelete)
                    },
                    enabled = resultOfDelete.value !is Result.Loading
                ) {
                    Text(text = "Delete")
                }
            }
        )

        when (resultOfDelete.value) {
            is Result.Success -> {
                val message = (resultOfDelete.value as Result.Success).message
                Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
                showConfirmDialog.value = false
                onComplete()
            }

            is Result.Error -> {
                val message = (resultOfDelete.value as Result.Error).exception.message
                Toast.makeText(LocalContext.current, message, Toast.LENGTH_SHORT).show()
            }

            else -> {}
        }
    }
}
