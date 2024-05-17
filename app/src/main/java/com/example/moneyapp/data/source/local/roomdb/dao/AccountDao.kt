package com.example.moneyapp.data.source.local.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.moneyapp.data.source.local.roomdb.entity.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAccount(account: Account): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateAccount(account: Account): Int

    @Delete
    suspend fun deleteAccount(account: Account)

    @Query("SELECT * FROM accounts WHERE userId = :userId ORDER BY name ASC")
    fun getUserAccountsByUserId(userId: Long): Flow<List<Account>>
}