package org.damascus.useCase.retrieve

import org.damascus.logic.MealRepository
import org.damascus.model.Meal
import kotlin.random.Random

class GetRandomPotatoMealsUseCase(
    private val repository: MealRepository
) {

    operator fun invoke(): List<Meal> = repository.getAllMeals()
        .filter { meal ->
            meal.ingredients.any { it.contains("potato", ignoreCase = true) }
        }
        .let { meals ->
            if (meals.size <= 10) meals else meals.shuffled(Random).take(10)
        }

}
