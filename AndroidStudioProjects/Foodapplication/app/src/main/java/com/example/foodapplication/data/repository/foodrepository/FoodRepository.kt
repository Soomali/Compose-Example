package com.example.foodapplication.data.repository.foodrepository

import com.example.foodapplication.data.Food

abstract class FoodRepository {
    abstract fun getFoods():List<Food>
}