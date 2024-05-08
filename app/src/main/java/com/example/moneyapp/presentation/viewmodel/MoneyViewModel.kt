package com.example.moneyapp.presentation.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyapp.data.repository.MoneyRepository
import com.example.moneyapp.data.source.local.roomdb.entity.User
import kotlinx.coroutines.launch

class MoneyViewModel(
    private val repository: MoneyRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _currentUser: MutableState<User?> = mutableStateOf(null)
    val currentUser get() = _currentUser

    init {
        loadCurrentUser()
    }

    fun loginUser(
        email: String,
        password: String,
        errorsList: SnapshotStateList<String>
    ) {
        viewModelScope.launch {
            _currentUser.value = repository.getUserByEmailAndPassword(email, password)
            if (currentUser.value == null) errorsList.add("Wrong email or password")
            else saveCurrentUser()
        }
    }

    fun signUpUser(
        name: String,
        email: String,
        password: String,
        errorsList: SnapshotStateList<String>
    ) {
        viewModelScope.launch {
            val userId = repository.insertUser(User(name, email, password))
            if (userId == -1L) {
                errorsList.add("Email is already in use")
            } else {
                _currentUser.value = repository.getUserById(userId)
                saveCurrentUser()
            }
        }
    }

    private fun saveCurrentUser() {
        currentUser.value?.let { user ->
            sharedPreferences.edit().apply {
                putLong("userId", user.id)
                apply()
            }
        }
    }

    private fun loadCurrentUser() {
        val userId: Long = sharedPreferences.getLong("userId", -1L)
        viewModelScope.launch {
            repository.getUserById(userId)?.let { _currentUser.value = it }
        }
    }

    fun removeCurrentUser() {
        _currentUser.value = null
        sharedPreferences.edit().remove("userId").apply()
    }
}