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

class MoneyRepository(
    private val userDao: UserDao,
    private val accountDao: AccountDao,
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val transactionCategoryDao: TransactionCategoryDao,
    private val bankDao: BankDao
) {
    // Get
    suspend fun getUserByEmailAndPassword(email: String, password: String): User =
        userDao.getUserByEmailAndPassword(email, password)
            ?: throw Exception("Wrong email or password")

    fun getBanks() = bankDao.getBanks()
    fun getUserAccountsByUserId(userId: Long) = accountDao.getUserAccountsByUserId(userId)
    fun getCategoriesByUserId(userId: Long) = categoryDao.getCategoriesByUserId(userId)
    fun getUserTransactionsByUserId(userId: Long) =
        transactionDao.getUserTransactionsByUserId(userId)

    // User
    suspend fun insertUser(user: User): User {
        val userId = userDao.insertUser(user)
        if (userId == -1L) throw Exception("Email already in use")
        return user.copy(id = userId)
    }

    suspend fun updateUser(user: User) {
        val success = userDao.updateUser(user) == 1
        if (!success) throw Exception("Failed to update user")
    }

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    // Account
    suspend fun insertAccount(account: Account): Account {
        val accountId = accountDao.insertAccount(account)
        if (accountId == -1L) throw Exception("Name already in use")
        return account.copy(id = accountId)
    }

    suspend fun updateAccount(account: Account) {
        val success = accountDao.updateAccount(account) == 1
        if (!success) throw Exception("Failed to update account")
    }

    suspend fun deleteAccount(account: Account) = accountDao.deleteAccount(account)

    // Transaction
    suspend fun insertTransaction(
        transaction: Transaction,
        account: Account,
        categoryId: Long?
    ): Long {
        val transactionId = transactionDao.insertTransaction(transaction)
        if (transactionId == -1L) throw Exception("The transaction has not been saved")
        else {
            val successUpdatedAccount = accountDao.updateAccount(
                account.copy(
                    balance = account.balance + transaction.getCostOperation()
                )
            ) == 1
            if (!successUpdatedAccount) {
                transactionDao.deleteTransaction(transaction)
                throw Exception("The transaction has not been saved, because account balance has not been updated")
            }
            if (categoryId != null) {
                transactionCategoryDao.insertTransactionCategory(
                    TransactionCategory(
                        transactionId = transactionId,
                        categoryId = categoryId
                    )
                )
            }
        }
        return transactionId
    }


    suspend fun updateTransaction(
        oldTransaction: Transaction,
        newTransaction: Transaction,
        oldAccount: Account,
        newAccount: Account,
        oldCategory: Category?,
        newCategory: Category?
    ) {
        val successUpdateTransaction = transactionDao.updateTransaction(newTransaction) == 1
        if (!successUpdateTransaction) throw Exception("The transaction has not been updated")
        else {
            val successUpdatedAccount =
                if (oldTransaction.accountId != newTransaction.accountId) {
                    accountDao.updateAccount(
                        oldAccount.copy(
                            balance = oldAccount.balance - oldTransaction.getCostOperation()
                        )
                    ) + accountDao.updateAccount(
                        newAccount.copy(
                            balance = newAccount.balance + newTransaction.getCostOperation()
                        )
                    ) == 2
                } else if (oldTransaction.getCostOperation() != newTransaction.getCostOperation()) {
                    accountDao.updateAccount(
                        newAccount.copy(
                            balance = newAccount.balance - oldTransaction.cost + newTransaction.cost
                        )
                    ) == 1
                } else true

            if (!successUpdatedAccount) {
                transactionDao.updateTransaction(oldTransaction)
                throw Exception("The transaction has not been updated, because account balance has not been updated")
            }
            if (oldCategory == newCategory) return
            else if (oldCategory != null && newCategory == null) {
                transactionCategoryDao.deleteTransactionCategory(
                    TransactionCategory(
                        transactionId = oldTransaction.id,
                        categoryId = oldCategory.id
                    )
                )
            } else if (oldCategory == null && newCategory != null) {
                transactionCategoryDao.insertTransactionCategory(
                    TransactionCategory(
                        transactionId = newTransaction.id,
                        categoryId = newCategory.id
                    )
                )
            } else if (oldCategory != null && newCategory != null) {
                transactionCategoryDao.deleteTransactionCategory(
                    TransactionCategory(
                        transactionId = oldTransaction.id,
                        categoryId = oldCategory.id
                    )
                )
                transactionCategoryDao.insertTransactionCategory(
                    TransactionCategory(
                        transactionId = newTransaction.id,
                        categoryId = newCategory.id
                    )
                )
            }
        }
    }

    suspend fun deleteTransaction(transaction: Transaction) =
        transactionDao.deleteTransaction(transaction)

    // Category
    suspend fun insertCategory(category: Category): Category {
        val categoryId = categoryDao.insertCategory(category)
        if (categoryId == -1L) throw Exception("Name already in use")
        return category.copy(id = categoryId)
    }

    suspend fun updateCategory(category: Category) {
        val success = categoryDao.updateCategory(category) == 1
        if (!success) throw Exception("Name already in use")
    }

    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
}
