package org.damascus.model

data class MealOptions(
    val validMeal:List<Meal>,
    val correctMealIngredient:String,
    val wrongMealIngredients:List<String>
)
