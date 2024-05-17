package com.example.moneyapp.data.source.local.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moneyapp.data.source.local.roomdb.dao.AccountDao
import com.example.moneyapp.data.source.local.roomdb.dao.BankDao
import com.example.moneyapp.data.source.local.roomdb.dao.CategoryDao
import com.example.moneyapp.data.source.local.roomdb.dao.TransactionCategoryDao
import com.example.moneyapp.data.source.local.roomdb.dao.TransactionDao
import com.example.moneyapp.data.source.local.roomdb.dao.UserDao
import com.example.moneyapp.data.source.local.roomdb.entity.Account
import com.example.moneyapp.data.source.local.roomdb.entity.Bank
import com.example.moneyapp.data.source.local.roomdb.entity.Category
import com.example.moneyapp.data.source.local.roomdb.entity.Transaction
import com.example.moneyapp.data.source.local.roomdb.entity.TransactionCategory
import com.example.moneyapp.data.source.local.roomdb.entity.User

@Database(
    entities = [
        User::class,
        Account::class,
        Transaction::class,
        Category::class,
        TransactionCategory::class,
        Bank::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MoneyDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val accountDao: AccountDao
    abstract val moneyTransactionDao: TransactionDao
    abstract val categoryDao: CategoryDao
    abstract val transactionCategoryDao: TransactionCategoryDao
    abstract val bankDao: BankDao
}