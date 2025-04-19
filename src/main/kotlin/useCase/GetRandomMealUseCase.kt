package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal


class GetRandomMealUseCase(private val mealRepository: MealRepository) {

    fun getRandomMeal(): Meal {
        return mealRepository.getAllMeals().random()
    }
}
