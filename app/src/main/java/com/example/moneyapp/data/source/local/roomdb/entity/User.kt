package com.example.moneyapp.data.source.local.roomdb.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["email"], unique = true)]
)
data class User(
    val name: String,
    val email: String,
    val password: String,

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)
