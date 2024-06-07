package com.example.moneyapp.presentation.utils

sealed class Result<out T> {
    data object Pending : Result<Nothing>()
    data object Loading : Result<Nothing>()
    data class Success<out T>(val data: T, val message: String = "Successfully") : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}
