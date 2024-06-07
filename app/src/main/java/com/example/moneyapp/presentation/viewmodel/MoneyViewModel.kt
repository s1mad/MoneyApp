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
import com.example.moneyapp.data.source.local.roomdb.entity.User
import com.example.moneyapp.data.source.local.roomdb.relation.TransactionAndCategory
import com.example.moneyapp.presentation.utils.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MoneyViewModel(
    private val repository: MoneyRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {
    private val _currentUser: MutableState<Result<User>> = mutableStateOf(Result.Pending)
    val currentUser get() = _currentUser

    private val _accounts = MutableStateFlow<Result<List<Account>>>(Result.Pending)
    val accounts: StateFlow<Result<List<Account>>> = _accounts.asStateFlow()

    private val _transactions =
        MutableStateFlow<Result<List<TransactionAndCategory>>>(Result.Pending)
    val transactions: StateFlow<Result<List<TransactionAndCategory>>> = _transactions.asStateFlow()

    private val _categories = MutableStateFlow<Result<List<Category>>>(Result.Pending)
    val categories: StateFlow<Result<List<Category>>> = _categories.asStateFlow()

    private val _banks = MutableStateFlow<Result<List<Bank>>>(Result.Pending)
    val banks: StateFlow<Result<List<Bank>>> = _banks.asStateFlow()

    private val _activeAccount = mutableStateOf<Account?>(null)
    val activeAccount get() = _activeAccount

    init {
        loadCurrentUser()
    }

    // Auth ////////////////////////////////////////////////////////////////////////////////////////

    fun getUserData() {
        getUserAccounts()
        getUserTransactions()
        getUserCategories()
        getBanks()
    }

    fun updateActiveAccount(account: Account) {
        _activeAccount.value = account
    }

    fun getUserAccounts() {
        if (currentUser.value is Result.Success) {
            viewModelScope.launch {
                _accounts.value = Result.Loading
                try {
                    repository.getUserAccountsByUserId((currentUser.value as Result.Success).data.id)
                        .collect {
                            _accounts.value = Result.Success(it)
                        }
                } catch (e: Exception) {
                    _accounts.value = Result.Error(e)
                }
            }
        }
    }

    fun getUserTransactions() {
        if (currentUser.value is Result.Success) {
            viewModelScope.launch {
                _transactions.value = Result.Loading
                try {
                    repository.getUserTransactionsByUserId((currentUser.value as Result.Success).data.id)
                        .collect {
                            _transactions.value = Result.Success(it)
                        }
                } catch (e: Exception) {
                    _transactions.value = Result.Error(e)
                }
            }
        }
    }

    fun getUserCategories() {
        if (currentUser.value is Result.Success) {
            viewModelScope.launch {
                _categories.value = Result.Loading
                try {
                    repository.getCategoriesByUserId((currentUser.value as Result.Success).data.id)
                        .collect {
                            _categories.value = Result.Success(it)
                        }
                } catch (e: Exception) {
                    _categories.value = Result.Error(e)
                }
            }
        }
    }

    fun getBanks() {
        viewModelScope.launch {
            _banks.value = Result.Loading
            try {
                repository.getBanks().collect {
                    _banks.value = Result.Success(it)
                }
            } catch (e: Exception) {
                _banks.value = Result.Error(e)
            }
        }
    }

    fun loginUser(
        email: String,
        password: String,
        errorsList: SnapshotStateList<String>
    ) {
        viewModelScope.launch {
            _currentUser.value = Result.Loading
            try {
                val user = repository.getUserByEmailAndPassword(email, password)
                _currentUser.value = Result.Success(user, message = "User successfully logged in")
            } catch (e: Exception) {
                _currentUser.value = Result.Error(e)
                errorsList.add(e.message.toString())
            }
            getUserData()
            saveCurrentUser()
        }
    }

    fun signUpUser(
        name: String,
        email: String,
        password: String,
        errorsList: SnapshotStateList<String>
    ) {
        viewModelScope.launch {
            _currentUser.value = Result.Loading
            try {
                val user = repository.insertUser(User(name, email, password))
                _currentUser.value = Result.Success(
                    user,
                    message = "User successfully registered"
                )
            } catch (e: Exception) {
                _currentUser.value = Result.Error(e)
                errorsList.add(e.message.toString())
            }
            getUserData()
            saveCurrentUser()
        }
    }

    private fun saveCurrentUser() {
        if (currentUser.value is Result.Success) {
            val user = (currentUser.value as Result.Success).data
            sharedPreferences.edit().apply {
                putString("email", user.email)
                putString("password", user.password)
                apply()
            }
        }
    }

    private fun loadCurrentUser() {
        val email: String? = sharedPreferences.getString("email", null)
        val password: String? = sharedPreferences.getString("password", null)
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            _currentUser.value = Result.Error(NullPointerException())
            return
        }
        viewModelScope.launch {
            var user: User? = null
            try {
                user = repository.getUserByEmailAndPassword(email, password)
            } catch (e: Exception) {
                _currentUser.value = Result.Error(e)
            }
            if (user != null) {
                _currentUser.value = Result.Success(user)
                getUserData()
            }
        }
    }

    fun removeCurrentUser() {
        sharedPreferences.edit().remove("email").remove("password").apply()
        _currentUser.value = Result.Pending
        _accounts.value = Result.Pending
        _transactions.value = Result.Pending
        _categories.value = Result.Pending
    }

    // User ////////////////////////////////////////////////////////////////////////////////////////

    fun updateUser(
        user: User,
        result: MutableState<Result<User>> = mutableStateOf(Result.Pending)
    ): MutableState<Result<User>> {
        result.value = Result.Loading
        viewModelScope.launch {
            try {
                repository.updateUser(user)
                _currentUser.value =
                    Result.Success(user, message = "User name successfully updated")
            } catch (e: Exception) {
                result.value = Result.Error(e)
            }
        }
        return result
    }

    fun deleteUser(
        user: User,
        result: MutableState<Result<String>> = mutableStateOf(Result.Pending)
    ): MutableState<Result<String>> {
        result.value = Result.Loading
        viewModelScope.launch {
            try {
                repository.deleteUser(user)
                result.value = Result.Success("deleteUser", message = "User successfully deleted")
            } catch (e: Exception) {
                result.value = Result.Error(e)
            }
        }
        return result
    }

    // Account /////////////////////////////////////////////////////////////////////////////////////
    fun insertAccount(
        account: Account,
        result: MutableState<Result<Account>> = mutableStateOf(Result.Pending)
    ): MutableState<Result<Account>> {
        result.value = Result.Loading
        viewModelScope.launch {
            try {
                val insertedAccount = repository.insertAccount(account)
                result.value = Result.Success(
                    data = insertedAccount, message = "Account successfully added"
                )
            } catch (e: Exception) {
                result.value = Result.Error(e)
            }
        }
        return result
    }

    fun updateAccount(
        account: Account,
        result: MutableState<Result<Account>> = mutableStateOf(Result.Pending)
    ): MutableState<Result<Account>> {
        result.value = Result.Loading
        viewModelScope.launch {
            try {
                repository.updateAccount(account)
                result.value = Result.Success(account, message = "Account successfully updated")
            } catch (e: Exception) {
                result.value = Result.Error(e)
            }
        }
        return result
    }

    fun deleteAccount(
        account: Account,
        result: MutableState<Result<String>> = mutableStateOf(Result.Pending)
    ): MutableState<Result<String>> {
        result.value = Result.Loading
        viewModelScope.launch {
            try {
                repository.deleteAccount(account)
                result.value =
                    Result.Success("deleteAccount", message = "Account successfully deleted")
            } catch (e: Exception) {
                result.value
            }
        }
        return result
    }

    // Transaction /////////////////////////////////////////////////////////////////////////////////
    fun insertTransaction(
        transaction: Transaction,
        account: Account,
        categoryId: Long? = null,
        result: MutableState<Result<Transaction>> = mutableStateOf(Result.Pending)
    ): MutableState<Result<Transaction>> {
        result.value = Result.Loading
        viewModelScope.launch {
            try {
                val transactionId = repository.insertTransaction(transaction, account, categoryId)
                result.value = Result.Success(transaction.copy(id = transactionId))
            } catch (e: Exception) {
                result.value = Result.Error(e)
            }
        }
        return result
    }

    fun updateTransaction(
        oldTransaction: Transaction,
        newTransaction: Transaction,
        oldAccount: Account,
        newAccount: Account,
        oldCategory: Category?,
        newCategory: Category?,
        result: MutableState<Result<Transaction>> = mutableStateOf(Result.Pending)
    ): MutableState<Result<Transaction>> {
        result.value = Result.Loading
        viewModelScope.launch {
            try {
                repository.updateTransaction(
                    oldTransaction,
                    newTransaction,
                    oldAccount,
                    newAccount,
                    oldCategory,
                    newCategory
                )
                result.value = Result.Success(newTransaction, "Transaction successfully updated")
            } catch (e: Exception) {
                result.value = Result.Error(e)
            }
        }
        return result
    }

    fun deleteTransaction(
        transaction: Transaction,
        result: MutableState<Result<String>> = mutableStateOf(Result.Pending)
    ): MutableState<Result<String>> {
        result.value = Result.Loading
        viewModelScope.launch {
            try {
                repository.deleteTransaction(transaction)
                result.value =
                    Result.Success("deleteTransaction", "Transaction successfully deleted")
            } catch (e: Exception) {
                result.value = Result.Error(e)
            }
        }
        return result
    }

    // Category ////////////////////////////////////////////////////////////////////////////////////

    fun insertCategory(
        category: Category,
        result: MutableState<Result<Category>> = mutableStateOf(Result.Pending)
    ): MutableState<Result<Category>> {
        result.value = Result.Loading
        viewModelScope.launch {
            try {
                val insertedCategory = repository.insertCategory(category)
                result.value = Result.Success(
                    data = insertedCategory,
                    message = "Category successfully added"
                )
            } catch (e: Exception) {
                result.value = Result.Error(e)
            }
        }
        return result
    }

    fun updateCategory(
        category: Category,
        result: MutableState<Result<Category>> = mutableStateOf(Result.Pending)
    ): MutableState<Result<Category>> {
        result.value = Result.Loading
        viewModelScope.launch {
            try {
                repository.updateCategory(category)
                result.value = Result.Success(
                    data = category,
                    message = "Category successfully added"
                )
            } catch (e: Exception) {
                result.value = Result.Error(e)
            }
        }
        return result
    }

    fun deleteCategory(
        category: Category,
        result: MutableState<Result<String>> = mutableStateOf(Result.Pending)
    ): MutableState<Result<String>> {
        result.value = Result.Loading
        viewModelScope.launch {
            try {
                repository.deleteCategory(category)
                result.value =
                    Result.Success("deleteCategory", message = "Category successfully deleted")
            } catch (e: Exception) {
                result.value = Result.Error(e)
            }
        }
        return result
    }
}
