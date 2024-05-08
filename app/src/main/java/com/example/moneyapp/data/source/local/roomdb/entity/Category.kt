package com.example.moneyapp.data.source.local.roomdb.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "categories",
    indices = [Index(value = ["name"], unique = true)],
)
data class Category(
    val name: String,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
