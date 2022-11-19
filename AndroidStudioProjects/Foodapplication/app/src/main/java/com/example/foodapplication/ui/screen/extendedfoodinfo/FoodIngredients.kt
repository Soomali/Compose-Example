package com.example.foodapplication.ui.screen.extendedfoodinfo

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun FoodIngredients(@StringRes foodIngredients:Int,modifier: Modifier = Modifier ){
    stringResource(id = foodIngredients,).
    split('*').
    filter{ it.isNotEmpty() }.
    map{

        Text(it,modifier=modifier.fillMaxWidth().padding(horizontal = 24.dp))
    }
}