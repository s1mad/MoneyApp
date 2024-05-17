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
    val cost: Double,
    val place: String,
    val comment: String,
    val date: Long,
    val dataCreated: Long = Calendar.getInstance().timeInMillis,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
) {
    fun getCostOperation() = if (operation == Operation.EXPENSE) -cost else cost
    companion object {
        fun getCostOperation(operation: Operation, cost: Double) =
            if (operation == Operation.EXPENSE) -cost else cost
    }
}

enum class Operation {
    EXPENSE,
    INCOME
}
