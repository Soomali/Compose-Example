package com.example.foodapplication.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.foodapplication.R

data class Food(
    @StringRes val foodName:Int,
    @StringRes val foodDuration:Int,
    @StringRes val foodIngredients:Int,
    @DrawableRes val foodPhoto:Int,
    @StringRes val foodRecipe:Int,
    )

