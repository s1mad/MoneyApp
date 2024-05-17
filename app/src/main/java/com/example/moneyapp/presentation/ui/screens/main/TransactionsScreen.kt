package com.example.moneyapp.presentation.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.moneyapp.data.source.local.roomdb.entity.Operation
import com.example.moneyapp.data.source.local.roomdb.relation.TransactionAndCategory
import com.example.moneyapp.presentation.ui.screens.main.components.transactions.UpdateTransactionDialog
import com.example.moneyapp.presentation.ui.screens.main.utils.formatDoubleToString
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel
import java.util.Calendar

@Composable
fun TransactionsScreen(
    modifier: Modifier,
    viewModel: MoneyViewModel,
    accountId: Long? = null
) {
    val transactions = if (accountId == null) viewModel.transactions.collectAsState().value
    else viewModel.transactions.collectAsState().value.filter { it.transaction.accountId == accountId }

    val groupedTransactions = transactions.groupBy { it.transaction.date }

    val accounts = viewModel.accounts.collectAsState().value

    val activeTransaction = remember { mutableStateOf<TransactionAndCategory?>(null) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 8.dp)
    ) {
        items(groupedTransactions.keys.toList()){date ->
            Column {
                Text(
                    text = formatDateToString(date),
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )
                groupedTransactions[date]!!.forEach { transaction ->
                    Column(
                        modifier = Modifier
                            .clickable {
                                activeTransaction.value = transaction
                            }
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.Top
                    ) {
                        Row(modifier = Modifier.wrapContentSize()) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = transaction.category?.name ?: "Unselected",
                                fontSize = 18.sp
                            )
                            Text(
                                text = "${formatDoubleToString(transaction.transaction.getCostOperation())} ₽",
                                fontSize = 18.sp,
                                color = if (transaction.transaction.operation == Operation.INCOME) Color.Green else Color.Unspecified
                            )
                        }
                        Text(
                            text = accounts.find { it.id == transaction.transaction.accountId }?.name
                                ?: "Not Found",
                            color = Color.Gray,
                            maxLines = 1
                        )
                        if (transaction.transaction.place.isNotEmpty()) {
                            Text(
                                text = transaction.transaction.place,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        if (transaction.transaction.comment.isNotEmpty()) {
                            Text(
                                text = transaction.transaction.comment,
                                color = Color.Gray,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
    UpdateTransactionDialog(
        transaction = activeTransaction,
        accounts = viewModel.accounts.collectAsState().value,
        categories = viewModel.categories.collectAsState().value,
        viewModel = viewModel
    )
}

fun formatDateToString(initialDate: Long): String {
    val date = Calendar.getInstance().apply { timeInMillis = initialDate }

    val monthNames = arrayOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    val daysOfWeek =
        arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")

    val todayOrYesterday =
        if (Calendar.getInstance().get(Calendar.YEAR) == date.get(Calendar.YEAR) &&
            Calendar.getInstance().get(Calendar.DAY_OF_YEAR) - date.get(Calendar.DAY_OF_YEAR) <= 1
        ) {
            if (Calendar.getInstance().get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR)
            )
                "Today, " else "Yesterday, "
        } else ""

    val monthAndDay =
        "${monthNames[date.get(Calendar.MONTH)]} ${date.get(Calendar.DAY_OF_MONTH)}"

    val year = if (date.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) ""
    else ", " + date.get(Calendar.YEAR).toString()

    val dayOfWeek =
        if (todayOrYesterday.isEmpty()) ", " + daysOfWeek[date.get(Calendar.DAY_OF_WEEK) - 1] else ""


    return "$todayOrYesterday$monthAndDay$year$dayOfWeek"
}
