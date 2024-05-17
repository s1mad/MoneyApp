package com.example.moneyapp.presentation.ui.screens.main.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionsExposedDropdownMenuBox(
    options: Map<Long, String>,
    selectedOptionId: MutableState<Long?>,
    label: String,
    isNullableOption: Boolean = true
) {
    val expanded = remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded.value,
        onExpandedChange = { expanded.value = it }
    ) {
        OutlinedTextField(
            value = options.getOrDefault(selectedOptionId.value, "Unselected"),
            onValueChange = { },
            label = { Text(text = label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded.value) },
            modifier = Modifier.menuAnchor(),
            readOnly = true
        )
        ExposedDropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            if (isNullableOption) {
                DropdownMenuItem(
                    text = { Text(text = "Unselected") },
                    onClick = {
                        selectedOptionId.value = null
                        expanded.value = false
                    }
                )
            }
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.value) },
                    onClick = {
                        selectedOptionId.value = option.key
                        expanded.value = false
                    }
                )
            }
        }
    }
}
