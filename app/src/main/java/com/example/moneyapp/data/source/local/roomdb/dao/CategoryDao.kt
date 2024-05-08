package com.example.moneyapp.data.source.local.roomdb.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.moneyapp.data.source.local.roomdb.entity.Category
import com.example.moneyapp.data.source.local.roomdb.relation.CategoryWithTransactions

@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsertCategory(category: Category)

    @Delete
    suspend fun deleteCategory(category: Category)

    @Transaction
    @Query("SELECT * FROM categories")
    suspend fun getCategoryWithTransactions() : List<CategoryWithTransactions>
}