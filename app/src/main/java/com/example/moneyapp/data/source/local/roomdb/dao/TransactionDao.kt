package com.example.moneyapp.data.source.local.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction as TransactionRoom
import androidx.room.Upsert
import com.example.moneyapp.data.source.local.roomdb.entity.Transaction
import com.example.moneyapp.data.source.local.roomdb.relation.TransactionWithCategories

@Dao
interface TransactionDao {
    @Upsert
    suspend fun upsertTransaction(transaction: Transaction)

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @TransactionRoom
    @Query("SELECT * FROM transactions")
    suspend fun getTransactionWithCategories() : List<TransactionWithCategories>
}