package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class SeafoodMealsSortedByProteinContentUSeCase(private val mealRepository: MealRepository) {
    fun sortSeafoodMealsByProteinContent(): List<Meal> {
        return mealRepository.getAllMeals()
            .filter {meal->meal.isSeafoodMeal()}
            .sortedByDescending { meal->meal.nutrition.protein }
    }

    private fun Meal.isSeafoodMeal():Boolean{
        return this.tags.any{tag->tag.contains("seafood",ignoreCase = true)}
    }


}