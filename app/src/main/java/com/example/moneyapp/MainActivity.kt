package com.example.moneyapp

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.example.moneyapp.data.repository.MoneyRepository
import com.example.moneyapp.data.source.local.roomdb.MoneyDatabase
import com.example.moneyapp.presentation.ui.graphs.RootNavigationGraph
import com.example.moneyapp.presentation.ui.theme.MoneyAppTheme
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

class MainActivity : ComponentActivity() {
    private val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            MoneyDatabase::class.java,
            "MoneyDatabase.db"
        ).build()
    }

    private val viewModel by viewModels<MoneyViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MoneyViewModel(
                        repository = MoneyRepository(
                            database.userDao,
                            database.accountDao,
                            database.moneyTransactionDao,
                            database.categoryDao,
                            database.transactionCategoryDao
                        ),
                        sharedPreferences = this@MainActivity.getSharedPreferences(
                            "preferences",
                            Context.MODE_PRIVATE
                        )
                    ) as T
                }
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MoneyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RootNavigationGraph(navController = rememberNavController(), viewModel = viewModel)
                }
            }
        }
    }
}
