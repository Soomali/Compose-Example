package com.example.foodapplication.ui.screen.extendedfoodinfo

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

@Composable
fun FoodRecipe(@StringRes foodRecipe:Int, modifier: Modifier = Modifier ){
    stringResource(id = foodRecipe).split('\n').forEachIndexed { index, s ->
        Row(modifier=modifier) {
          Text("${index + 1}", fontWeight = FontWeight.Bold, textAlign = TextAlign.Start)

            Text( " - $s" ,textAlign = TextAlign.Start)
        }
    }

}
