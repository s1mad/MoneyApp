package com.example.moneyapp.presentation.ui.screens.main.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

@Composable
fun ConfirmDeleteDialog(
    showConfirmDialog: MutableState<Boolean>,
    delete: () -> Unit,
    onComplete: () -> Unit = {}
) {
    if (showConfirmDialog.value) {
        AlertDialog(
            title = { Text(text = "Confirm delete") },
            onDismissRequest = { showConfirmDialog.value = false },
            dismissButton = {
                Button(onClick = { showConfirmDialog.value = false }) {
                    Text(text = "Cancel")
                }
            },
            confirmButton = {
                Button(onClick = {
                    delete()
                    showConfirmDialog.value = false
                    onComplete()
                }) {
                    Text(text = "Delete")
                }
            }
        )
    }
}
