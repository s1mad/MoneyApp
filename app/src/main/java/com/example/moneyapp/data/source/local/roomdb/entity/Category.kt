package com.example.moneyapp.data.source.local.roomdb.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [Index(value = ["name"], unique = true), Index("userId")],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onUpdate = ForeignKey.CASCADE,
            onDelete = ForeignKey.CASCADE
        ),
    ],
)
data class Category(
    val userId: Long,
    val name: String,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
