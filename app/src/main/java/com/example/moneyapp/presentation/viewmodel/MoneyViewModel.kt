package com.example.moneyapp.presentation.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moneyapp.data.repository.MoneyRepository
import com.example.moneyapp.data.source.local.roomdb.entity.Account
import com.example.moneyapp.data.source.local.roomdb.entity.Bank
import com.example.moneyapp.data.source.local.roomdb.entity.Category
import com.example.moneyapp.data.source.local.roomdb.entity.Transaction
import com.example.moneyapp.data.source.local.roomdb.entity.TransactionCategory
import com.example.moneyapp.data.source.local.roomdb.entity.User
import com.example.moneyapp.data.source.local.roomdb.relation.TransactionAndCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoneyViewModel(
    private val repository: MoneyRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _currentUser: MutableState<User?> = mutableStateOf(null)
    val currentUser get() = _currentUser

    private val _accounts = MutableStateFlow<List<Account>>(emptyList())
    val accounts: StateFlow<List<Account>> = _accounts.asStateFlow()

    private val _transactions = MutableStateFlow<List<TransactionAndCategory>>(emptyList())
    val transactions: StateFlow<List<TransactionAndCategory>> = _transactions.asStateFlow()

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _banks = MutableStateFlow<List<Bank>>(emptyList())
    val banks: StateFlow<List<Bank>> = _banks.asStateFlow()

    private val _activeAccount = mutableStateOf(Account(0, null, "Loading", 0.0))
    val activeAccount get() = _activeAccount

    init {
        loadCurrentUser()
        getUserData()
    }

    // Auth ////////////////////////////////////////////////////////////////////////////////////////

    fun getUserData() {
        currentUser.value?.let { user ->
            viewModelScope.launch {
                repository.getUserAccountsByUserId(user.id).collect {
                    _accounts.value = it
                }
            }
            viewModelScope.launch {
                repository.getUserTransactionsByUserId(user.id).collect {
                    _transactions.value = it
                }
            }
            viewModelScope.launch {
                repository.getCategoriesByUserId(user.id).collect {
                    _categories.value = it
                }
            }
            viewModelScope.launch {
                repository.getBanks().collect {
                    _banks.value = it
                }
            }
        }
    }

    fun loginUser(
        email: String,
        password: String,
        errorsList: SnapshotStateList<String>
    ) {
        viewModelScope.launch {
            _currentUser.value = repository.getUserByEmailAndPassword(email, password)
            if (currentUser.value == null) errorsList.add("Wrong email or password")
            else {
                getUserData()
                saveCurrentUser()
            }
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
                getUserData()
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
            repository.getUserById(userId)?.let {
                _currentUser.value = it
                getUserData()
            }
        }
    }

    fun removeCurrentUser() {
        sharedPreferences.edit().remove("userId").apply()
        _currentUser.value = null
        _accounts.value = emptyList()
        _transactions.value = emptyList()
    }

    // User ////////////////////////////////////////////////////////////////////////////////////////

    fun updateUser(
        user: User,
        isSuccessfullyUpdate: MutableState<Boolean?> = mutableStateOf(null)
    ) {
        viewModelScope.launch {
            isSuccessfullyUpdate.value = repository.updateUser(user) == 1
            if (isSuccessfullyUpdate.value == true) _currentUser.value = user
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }
    }

    // Account /////////////////////////////////////////////////////////////////////////////////////
    fun insertAccount(
        account: Account,
        isSuccessfullyAdded: MutableState<Boolean?> = mutableStateOf(null)
    ) {
        viewModelScope.launch {
            isSuccessfullyAdded.value = repository.insertAccount(account) != -1L

        }
    }

    fun updateAccount(
        account: Account,
        isSuccessfullyUpdate: MutableState<Boolean?> = mutableStateOf(null)
    ) {
        viewModelScope.launch {
            isSuccessfullyUpdate.value = repository.updateAccount(account) == 1
            if (isSuccessfullyUpdate.value == true) _activeAccount.value = account
        }
    }

    fun deleteAccount(account: Account) {
        viewModelScope.launch {
            repository.deleteAccount(account)
        }
    }

    fun updateActiveAccount(account: Account) {
        _activeAccount.value = account
    }

    // Transaction /////////////////////////////////////////////////////////////////////////////////
    fun insertTransaction(
        transaction: Transaction,
        transactionId: MutableState<Long?> = mutableStateOf(null)
    ) {
        viewModelScope.launch {
            transactionId.value = repository.insertTransaction(transaction)
        }
    }

    fun updateTransaction(
        transaction: Transaction,
        isSuccessfullyUpdate: MutableState<Boolean?> = mutableStateOf(null)
    ) {
        viewModelScope.launch {
            isSuccessfullyUpdate.value = repository.updateTransaction(transaction) == 1
        }
    }

    fun deleteTransaction(transaction: Transaction) {
        viewModelScope.launch {
            repository.deleteTransaction(transaction)
        }
    }

    // Category ////////////////////////////////////////////////////////////////////////////////////

    fun insertCategory(
        category: Category,
        isSuccessfullyAdded: MutableState<Boolean?> = mutableStateOf(null)
    ) {
        viewModelScope.launch {
            isSuccessfullyAdded.value = repository.insertCategory(category) != -1L
        }
    }

    fun updateCategory(
        category: Category,
        isSuccessfullyUpdate: MutableState<Boolean?> = mutableStateOf(null)
    ) {
        viewModelScope.launch {
            isSuccessfullyUpdate.value = repository.updateCategory(category) == 1
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }

    // Category ////////////////////////////////////////////////////////////////////////////////////

    fun insertTransactionCategory(
        transactionCategory: TransactionCategory,
        isSuccessfullyAdded: MutableState<Boolean?> = mutableStateOf(null)
    ) {
        viewModelScope.launch {
            isSuccessfullyAdded.value =
                repository.insertTransactionCategory(transactionCategory) != -1L
        }
    }

    fun updateTransactionCategory(
        old: TransactionCategory,
        new: TransactionCategory,
        isSuccessfullyUpdated: MutableState<Boolean?> = mutableStateOf(null)
    ) {
        viewModelScope.launch {
            isSuccessfullyUpdated.value = repository.replaceTransactionCategory(old, new) != -1L
        }
    }

    fun deleteTransactionCategory(
        transactionCategory: TransactionCategory
    ) {
        viewModelScope.launch {
            repository.deleteTransactionCategory(transactionCategory)
        }
    }
}
