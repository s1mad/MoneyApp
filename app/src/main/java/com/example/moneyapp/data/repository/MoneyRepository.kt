package com.example.moneyapp.data.repository

import com.example.moneyapp.data.source.local.roomdb.dao.AccountDao
import com.example.moneyapp.data.source.local.roomdb.dao.CategoryDao
import com.example.moneyapp.data.source.local.roomdb.dao.TransactionCategoryDao
import com.example.moneyapp.data.source.local.roomdb.dao.TransactionDao
import com.example.moneyapp.data.source.local.roomdb.dao.UserDao
import com.example.moneyapp.data.source.local.roomdb.entity.User

class MoneyRepository(
    private val userDao: UserDao,
    private val accountDao: AccountDao,
    private val moneyTransactionDao: TransactionDao,
    private val categoryDao: CategoryDao,
    private val transactionCategoryDao: TransactionCategoryDao
) {
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
    suspend fun getUserById(id: Long) = userDao.getUserById(id)
    suspend fun getUserByEmailAndPassword(email: String, password: String) =
        userDao.getUserByEmailAndPassword(email, password)
}