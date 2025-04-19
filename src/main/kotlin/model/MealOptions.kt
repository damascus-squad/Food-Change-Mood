package org.damascus.model

data class MealOptions(
    val correctMealIngredient:String,
    val wrongMealIngredients:List<String>
)
