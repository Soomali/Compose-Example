package com.example.foodapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.foodapplication.data.repository.foodrepository.FoodRepository

class FoodViewModelFactory(private val foodRepository: FoodRepository):ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return FoodViewModel(foodRepository) as T
    }
}
