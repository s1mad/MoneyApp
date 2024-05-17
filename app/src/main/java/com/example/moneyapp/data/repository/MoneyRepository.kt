package com.example.moneyapp.data.repository

import com.example.moneyapp.data.source.local.roomdb.dao.AccountDao
import com.example.moneyapp.data.source.local.roomdb.dao.BankDao
import com.example.moneyapp.data.source.local.roomdb.dao.CategoryDao
import com.example.moneyapp.data.source.local.roomdb.dao.TransactionCategoryDao
import com.example.moneyapp.data.source.local.roomdb.dao.TransactionDao
import com.example.moneyapp.data.source.local.roomdb.dao.UserDao
import com.example.moneyapp.data.source.local.roomdb.entity.Account
import com.example.moneyapp.data.source.local.roomdb.entity.Category
import com.example.moneyapp.data.source.local.roomdb.entity.Transaction
import com.example.moneyapp.data.source.local.roomdb.entity.TransactionCategory
import com.example.moneyapp.data.source.local.roomdb.entity.User
import com.example.moneyapp.data.source.local.roomdb.relation.TransactionAndCategory
import kotlinx.coroutines.flow.Flow

class MoneyRepository(
    private val userDao: UserDao,
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val transactionCategoryDao: TransactionCategoryDao,
    private val bankDao: BankDao
) {
    // User
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    suspend fun getUserById(id: Long) = userDao.getUserById(id)
    suspend fun getUserByEmailAndPassword(email: String, password: String) =
        userDao.getUserByEmailAndPassword(email, password)

    // Account
    suspend fun insertAccount(account: Account) = accountDao.insertAccount(account)
    suspend fun updateAccount(account: Account) = accountDao.updateAccount(account)
    suspend fun deleteAccount(account: Account) = accountDao.deleteAccount(account)
    fun getUserAccountsByUserId(userId: Long) = accountDao.getUserAccountsByUserId(userId)

    // Transaction
    suspend fun insertTransaction(transaction: Transaction) =
        transactionDao.insertTransaction(transaction)

    suspend fun updateTransaction(transaction: Transaction) =
        transactionDao.updateTransaction(transaction)

    suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.deleteTransaction(transaction)

    fun getUserTransactionsByUserId(userId: Long): Flow<List<TransactionAndCategory>> =
        transactionDao.getUserTransactionsByUserId(userId)

    // Category
    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
    fun getCategoriesByUserId(userId: Long) = categoryDao.getCategoriesByUserId(userId)

    // TransactionCategory
    suspend fun insertTransactionCategory(transactionCategory: TransactionCategory) =
        transactionCategoryDao.insertTransactionCategory(transactionCategory)

    suspend fun replaceTransactionCategory(old: TransactionCategory, new: TransactionCategory): Long {
        transactionCategoryDao.deleteTransactionCategory(old)
        return transactionCategoryDao.insertTransactionCategory(new)
    }

    suspend fun deleteTransactionCategory(transactionCategory: TransactionCategory) =
        transactionCategoryDao.deleteTransactionCategory(transactionCategory)

    // Bank
    fun getBanks() = bankDao.getBanks()
}