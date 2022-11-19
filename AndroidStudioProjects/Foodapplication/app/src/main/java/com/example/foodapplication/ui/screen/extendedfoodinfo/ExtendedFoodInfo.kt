package com.example.foodapplication.ui.screen.extendedfoodinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.example.foodapplication.data.Food

@Composable
fun ExtendedFoodInfo(food: Food,onClickRecipe:((Food) -> Unit)? = null){

    Column(modifier=Modifier.verticalScroll(rememberScrollState()),horizontalAlignment = Alignment.CenterHorizontally) {
        FoodImage(resource = food.foodPhoto, modifier = Modifier
            .size(250.dp)
            .padding(top=24.dp)
            .clip(CircleShape))
        FoodDataDisplay(foodName = food.foodName, foodDuration =food.foodDuration,modifier = Modifier.padding(vertical = 24.dp) )
        if(onClickRecipe == null) {
            FoodRecipe(foodRecipe = food.foodRecipe, modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp))
        } else {
            FoodIngredients(foodIngredients = food.foodIngredients, modifier = Modifier.padding(vertical = 12.dp))
            FoodRecipeButton(onClick = { onClickRecipe(food) }, title = "Tarife Git", modifier = Modifier.padding(bottom = 24.dp) )
        }
    }
}