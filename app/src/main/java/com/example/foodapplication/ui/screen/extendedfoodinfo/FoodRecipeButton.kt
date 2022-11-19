package com.example.foodapplication.ui.screen.extendedfoodinfo

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FoodRecipeButton(onClick:() -> Unit,title:String,modifier: Modifier=Modifier){
    Button(onClick = onClick, modifier = modifier) {
        Text(title)
    }
}