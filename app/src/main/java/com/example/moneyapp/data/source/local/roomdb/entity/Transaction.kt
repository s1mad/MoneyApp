package com.example.moneyapp.data.source.local.roomdb.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Calendar

@Entity(
    tableName = "transactions",
    indices = [Index("accountId")],
    foreignKeys = [
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ],
)
data class Transaction(
    val accountId: Long,
    val operation: Operation,
    val cost: Long,
    val date: Long = Calendar.getInstance().timeInMillis,
    val place: String,
    val comment: String,
    val dataCreated: Long = Calendar.getInstance().timeInMillis,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)

enum class Operation {
    EXPENSE,
    INCOME
}
