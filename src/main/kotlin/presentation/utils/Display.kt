package org.damascus.presentation.utils

import org.damascus.model.Meal
import org.damascus.utils.TerminalColor
import org.damascus.utils.withStyle

interface Display {
    //    fun displayMenu(options:List<MenuAction>)
    fun meals(meals: List<Meal>)
    fun mealsName(meals: List<Meal>)
    fun specificMeal(meal: Meal)
    fun displayMealByProtine(meals: List<Meal>) {
        meals.forEach { it ->
            println(
                "Calories: ${it.nutrition.calories}, Protein: ${it.nutrition.protein} | Meal name: ${it.name}".withStyle(
                    TerminalColor.Green
                )
            )
        }
    }
}