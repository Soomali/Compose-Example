package com.example.foodapplication.ui.screen.extendedfoodinfo

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun FoodDataDisplay(@StringRes foodName:Int,@StringRes foodDuration:Int,modifier: Modifier =Modifier){
    Column(modifier=modifier.fillMaxWidth()) {
        Text(stringResource(id = foodName), modifier = Modifier.fillMaxWidth(),fontSize=40.sp, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        Text("Tahmini Süre", fontSize = 24.sp,modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Light)
        Text(stringResource(id = foodDuration),fontSize=20.sp,modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
    }
}