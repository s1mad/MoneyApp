package com.example.moneyapp.data.source.local.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Upsert
import com.example.moneyapp.data.source.local.roomdb.entity.TransactionCategory

@Dao
interface TransactionCategoryDao {
    @Upsert
    suspend fun upsertTransactionCategory(transactionCategory: TransactionCategory)

    @Delete
    suspend fun deleteTransactionCategory(transactionCategory: TransactionCategory)
}