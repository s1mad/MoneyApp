package com.example.moneyapp.data.source.local.roomdb.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.moneyapp.data.source.local.roomdb.entity.Account
import com.example.moneyapp.data.source.local.roomdb.entity.User

data class UserWithAccounts(
    @Embedded
    val user: User,

    @Relation(
        parentColumn = "id",
        entityColumn = "userId",
    )
    val accounts: List<Account>
)