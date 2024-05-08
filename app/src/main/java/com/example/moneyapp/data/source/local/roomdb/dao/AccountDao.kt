package com.example.moneyapp.data.source.local.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.moneyapp.data.source.local.roomdb.entity.Account
import com.example.moneyapp.data.source.local.roomdb.relation.AccountWithTransactions

@Dao
interface AccountDao {
    @Upsert
    suspend fun upsertAccount(account: Account)

    @Delete
    suspend fun deleteAccount(account: Account)

    @Transaction
    @Query("SELECT * FROM accounts")
    suspend fun getAccountWithTransactions() : List<AccountWithTransactions>
}