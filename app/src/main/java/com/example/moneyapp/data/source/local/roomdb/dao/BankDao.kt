package com.example.moneyapp.data.source.local.roomdb.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.moneyapp.data.source.local.roomdb.entity.Bank
import kotlinx.coroutines.flow.Flow

@Dao
interface BankDao {
    @Query("SELECT * FROM banks ORDER BY name ASC")
    fun getBanks(): Flow<List<Bank>>
}