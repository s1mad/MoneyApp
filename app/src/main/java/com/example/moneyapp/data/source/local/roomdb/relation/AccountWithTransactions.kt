package com.example.moneyapp.data.source.local.roomdb.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.moneyapp.data.source.local.roomdb.entity.Account
import com.example.moneyapp.data.source.local.roomdb.entity.Transaction


data class AccountWithTransactions(
    @Embedded
    val account: Account,

    @Relation(
        parentColumn = "id",
        entityColumn = "accountId"
    )
    val moneyTransactions: List<Transaction>
)
