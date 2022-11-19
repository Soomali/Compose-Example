package com.example.foodapplication.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.NavigateNext
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.foodapplication.data.Food
import com.example.foodapplication.ui.theme.Red200


@Composable
fun FoodItem(food:Food,onClick: (Food) -> Unit,modifier:Modifier = Modifier,) {
    var isExpanded = rememberSaveable {
        mutableStateOf(false)
    }
    Column(modifier= modifier
        .padding(15.dp)
        .animateContentSize(
            spring(
                dampingRatio = Spring.DampingRatioLowBouncy,
                stiffness = Spring.StiffnessMediumLow
            )
        ) ) {
        Row {
            Image(painter = painterResource(id = food.foodPhoto), contentDescription = "",
                contentScale = ContentScale.Crop, modifier = modifier
                    .clip(CircleShape)
                    .size(72.dp))
            Column(modifier=Modifier.weight(1f).padding(start=8.dp)) {
                Text(stringResource(id = food.foodName), modifier = Modifier.padding(top = 4.dp,bottom=12.dp))
                Text(stringResource(id = food.foodDuration))
            }

            IconButton(onClick = {

                isExpanded.value = !isExpanded.value
            }) {
                Icon(imageVector =if(isExpanded.value) Icons.Filled.ExpandLess  else Icons.Filled.ExpandMore
                    , contentDescription = "")
            }
        }

        if(isExpanded.value) {
            val ingredients = stringResource(id = food.foodIngredients)
            ingredients.split("*").forEach {
                Text(it)
            }
            IconButton(onClick = { onClick(food) },
                modifier = Modifier.align(Alignment.End).padding(top=16.dp).border(BorderStroke(2.dp, Red200), shape = CircleShape)) {
                Icon(Icons.Filled.NavigateNext,"",)
            }
        }
    }
}