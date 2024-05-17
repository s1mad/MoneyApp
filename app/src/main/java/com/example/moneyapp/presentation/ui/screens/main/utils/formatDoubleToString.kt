package com.example.moneyapp.presentation.ui.screens.main.utils

import java.text.NumberFormat
import java.util.Locale

fun formatDoubleToString(number: Double): String {
    val format = NumberFormat.getNumberInstance(Locale.getDefault())
    format.maximumFractionDigits = 2
    format.minimumFractionDigits = if (number % 1.0 == 0.0) 0 else 2
    return format.format(number)
}