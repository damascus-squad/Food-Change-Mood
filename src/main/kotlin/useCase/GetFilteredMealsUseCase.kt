package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class GetFilteredMealsUseCase(
    private val mealRepository: MealRepository
) {

    operator fun invoke(predicate: (Meal) -> Boolean): List<Meal> {
        return mealRepository.getAllMeals().filter { meal -> predicate(meal) }
    }

}