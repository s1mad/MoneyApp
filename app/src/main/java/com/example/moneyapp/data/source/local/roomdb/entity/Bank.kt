package com.example.moneyapp.data.source.local.roomdb.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "banks",
    indices = [Index(value = ["name"], unique = true)]
)
data class Bank(
    val name: String,

    @PrimaryKey
    val id: Long = 0
)
