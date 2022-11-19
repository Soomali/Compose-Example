package com.example.foodapplication.data.repository.foodrepository

import com.example.foodapplication.R
import com.example.foodapplication.data.Food

class AssetFoodRepository:FoodRepository() {
    override fun getFoods(): List<Food> {
        val foods = listOf<Food>(
            Food(
                R.string.karniyarik,
                R.string.karniyarik_duration,
                R.string.karniyarik_ingredients,
                R.drawable.karniyarik,R.string.karniyarik_recipe),
            Food(
                R.string.fellah_kofte,
                R.string.fellah_kofte_duration,
                R.string.fellah_kofte_ingredients,
                R.drawable.fellah_kofte,R.string.fellah_kofte_recipe),
            Food(
                R.string.mercimek_koftesi,
                R.string.mercimek_koftesi_duration,
                R.string.mercimek_koftesi_ingredients,
                R.drawable.mercimek_koftesi,R.string.mercimek_koftesi_recipe),
            Food(R.string.tiramisu, R.string.tiramisu_duration, R.string.tiramisu_ingredients, R.drawable.tiramisu,R.string.tiramisu_recipe),
            Food(
                R.string.pilavli_patlican_kapama,
                R.string.pilavli_patlican_kapama_duration,
                R.string.pilavli_patlican_kapama_ingredients,
                R.drawable.pilavli_patlican_kapama,R.string.pilavli_patlican_kapama_recipe),
            Food(R.string.kisir, R.string.kisir_duration, R.string.kisir_ingredients, R.drawable.kisir,R.string.kisir_recipe),
            Food(
                R.string.mozaik_pasta,
                R.string.mozaik_pasta_duration,
                R.string.mozaik_pasta_ingredients,
                R.drawable.mozaik_pasta,R.string.mozaik_pasta_recipe),
            Food(
                R.string.patates_oturtma,
                R.string.patates_oturtma_duration,
                R.string.patates_oturtma_ingredients,
                R.drawable.patates_oturtma,R.string.patates_oturtma_recipe),
            Food(
                R.string.mayali_pogaca,
                R.string.mayali_pogaca_duration,
                R.string.mayali_pogaca_ingredients,
                R.drawable.mayali_pogaca,R.string.mayali_pogaca_recipe),
            Food(R.string.sarma, R.string.sarma_duration, R.string.sarma_ingredients, R.drawable.sarma,R.string.sarma_recipe),
        )
        return foods
    }

}