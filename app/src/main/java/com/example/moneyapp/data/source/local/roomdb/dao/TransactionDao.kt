package com.example.moneyapp.data.source.local.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Transaction as TransactionRoom
import com.example.moneyapp.data.source.local.roomdb.entity.Transaction
import com.example.moneyapp.data.source.local.roomdb.relation.TransactionAndCategory
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTransaction(transaction: Transaction): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateTransaction(transaction: Transaction): Int

    @Delete
    suspend fun deleteTransaction(transaction: Transaction)

    @TransactionRoom
    @Query("SELECT * FROM transactions WHERE accountId IN (SELECT id FROM accounts WHERE userId = :userId) ORDER BY date DESC")
    fun getUserTransactionsByUserId(userId: Long): Flow<List<TransactionAndCategory>>

    @TransactionRoom
    @Query("SELECT * FROM transactions WHERE accountId = :accountId ORDER BY date DESC")
    fun getUserTransactionsByAccountId(accountId: Long): Flow<List<TransactionAndCategory>>
}