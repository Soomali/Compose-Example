package com.example.foodapplication.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.example.foodapplication.data.repository.foodrepository.AssetFoodRepository
import com.example.foodapplication.ui.screen.extendedfoodinfo.ExtendedFoodInfo
import com.example.foodapplication.ui.theme.FoodApplicationTheme
import com.example.foodapplication.ui.theme.Red200
import com.example.foodapplication.ui.viewmodel.FoodViewModel
import com.example.foodapplication.ui.viewmodel.FoodViewModelFactory


@Composable
fun FoodApp(foodViewModel: FoodViewModel = viewModel(
    factory = FoodViewModelFactory(AssetFoodRepository())
)){
        //A surface container using the 'background' color from the theme
    val foods = foodViewModel.foods.collectAsState().value
    val currentFood = foodViewModel.currentFood.collectAsState().value
    foodViewModel.getFoods()
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(Red200)
                    .padding(start = 12.dp), verticalAlignment = Alignment.CenterVertically,){
                    if( navController.previousBackStackEntry  != null) {
                        IconButton(onClick = { navController.popBackStack() },) {
                            Icon(Icons.Filled.ArrowBack,"", tint = Color.White,)
                        }
                    }
                    val text:String = when (backStackEntry?.destination?.route) {
                        Routes.FOOD_RECIPE.name -> {
                            "${stringResource(id = currentFood!!.foodName)} tarifi"
                        }
                        Routes.FOOD_INFO.name -> {
                            stringResource(id = currentFood!!.foodName)
                        }
                        else -> "Yemekler"
                    }
                    Text(text, color = Color.White, fontSize = 24.sp)
                }
            }
        ) {
        NavHost(navController = navController, startDestination = Routes.FOOD_LIST.name) {
                composable(route=Routes.FOOD_LIST.name) {
                    FoodList(foods = foods) {
                        foodViewModel.selectFood(it)
                        navController.navigate(route = Routes.FOOD_INFO.name,)
                    }
                }
                composable(route=Routes.FOOD_INFO.name) {
                    ExtendedFoodInfo(currentFood!!,onClickRecipe = {
                        navController.navigate(route=Routes.FOOD_RECIPE.name)
                    })
                }
                composable(route= Routes.FOOD_RECIPE.name) {

                    ExtendedFoodInfo(currentFood!!)
                }
            }
        }
}