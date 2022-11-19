package com.example.foodapplication.ui.screen.extendedfoodinfo

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun FoodImage(@DrawableRes resource:Int,modifier:Modifier = Modifier){

    Image(painter = painterResource(id = resource),modifier=modifier, contentScale = ContentScale.Crop ,contentDescription = "")
}