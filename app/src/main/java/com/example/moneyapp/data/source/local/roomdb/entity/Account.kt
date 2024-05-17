package com.example.moneyapp.data.source.local.roomdb.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "accounts",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["bankId"]),
        Index(value = ["name"], unique = true),
    ],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Bank::class,
            parentColumns = ["id"],
            childColumns = ["bankId"]
        )
    ]
)
data class Account(
    val userId: Long,
    val bankId: Long?,
    val name: String,
    val balance: Double,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
