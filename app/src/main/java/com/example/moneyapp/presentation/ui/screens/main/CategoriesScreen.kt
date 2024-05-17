package com.example.moneyapp.presentation.ui.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.moneyapp.data.source.local.roomdb.entity.Category
import com.example.moneyapp.presentation.ui.screens.main.components.categories.UpdateCategoryDialog
import com.example.moneyapp.presentation.viewmodel.MoneyViewModel

@Composable
fun CategoriesScreen(
    viewModel: MoneyViewModel,
    modifier: Modifier
) {
    val categories = viewModel.categories.collectAsState().value
    val activeCategory = remember { mutableStateOf<Category?>(null) }

    LazyColumn(modifier = modifier.fillMaxSize()) {
        items(categories) { category ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .sizeIn(minHeight = 60.dp)
                    .clickable { activeCategory.value = category }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart) {
                Text(text = category.name)
            }
        }
    }
    UpdateCategoryDialog(
        category = activeCategory,
        viewModel = viewModel
    )
}