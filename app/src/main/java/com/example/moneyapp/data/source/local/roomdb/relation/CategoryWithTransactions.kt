package com.example.moneyapp.data.source.local.roomdb.relation

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.moneyapp.data.source.local.roomdb.entity.Category
import com.example.moneyapp.data.source.local.roomdb.entity.Transaction
import com.example.moneyapp.data.source.local.roomdb.entity.TransactionCategory

data class CategoryWithTransactions(
    @Embedded
    val category: Category,

    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            TransactionCategory::class,
            parentColumn = "categoryId",
            entityColumn = "transactionId"
        )
    )
    val transactions: List<Transaction>
)
