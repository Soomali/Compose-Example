package com.example.foodapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.foodapplication.data.Food
import com.example.foodapplication.data.repository.foodrepository.FoodRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FoodViewModel(private val repository: FoodRepository):ViewModel() {
    private val _foods = MutableStateFlow<List<Food>>(listOf())
    val foods:StateFlow<List<Food>> get() = _foods
    private val _currentFood = MutableStateFlow<Food?>(null)
    val currentFood:StateFlow<Food?> get() = _currentFood

    fun getFoods() {
        val result = repository.getFoods()
        _foods.value = result
    }
    fun selectFood(food:Food){
        _currentFood.value = food
    }
    fun resetFood(){
        _currentFood.value = null
    }


}