package com.example.foodapplication.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.foodapplication.data.Food

@Composable
fun FoodList(foods:List<Food>,onClick: (Food) -> Unit) {
    LazyColumn(modifier = Modifier.padding( horizontal = 12.dp)) {
        items(foods) {
            FoodItem(food = it,onClick)
        }
    }
}