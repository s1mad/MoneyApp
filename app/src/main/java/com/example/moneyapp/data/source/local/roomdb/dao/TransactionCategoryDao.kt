package com.example.moneyapp.data.source.local.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.example.moneyapp.data.source.local.roomdb.entity.TransactionCategory

@Dao
interface TransactionCategoryDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertTransactionCategory(transactionCategory: TransactionCategory)

    @Delete
    suspend fun deleteTransactionCategory(transactionCategory: TransactionCategory)
}