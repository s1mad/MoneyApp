package com.example.moneyapp.presentation.utils

fun getErrorsList(
    email: String,
    password: String,
    name: String? = null,
    confirmPassword: String? = null
): MutableList<String> = mutableListOf<String>().apply {
    name?.let {
        if (it.isBlank()) add("Name is empty")
        if (it.length < 3) add("Name less than 3")
        if (it.length > 32) add("Name greater than 32")
        if (!Regex("[a-zA-Z0-9]+").matches(it)) add("Name contains special characters")
    }

    if (email.isBlank()) add("Email is empty")
    else if (!email.contains("@") || email.length < 5 || !email.contains(".")) add("Email isn't email")

    if (password.isBlank()) add("Password is empty")
    else {
        if (password.length < 5) add("Password less than 5")
        else if (password.length > 32) add("password greater than 32")
        confirmPassword?.let {
            if (password != it) add("Passwords does not match")
        }
    }
}