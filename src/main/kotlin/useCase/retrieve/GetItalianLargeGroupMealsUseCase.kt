package org.damascus.useCase.retrieve

import org.damascus.logic.MealRepository
import org.damascus.model.Meal


class GetItalianLargeGroupMealsUseCase(
    private val repo: MealRepository
) {
    operator fun invoke(limit: Int = 50): List<Meal> {
        return repo.getAllMeals()
            .asSequence()
            .filter { isItalian(it) && isGoodForLargeGroups(it) }
            .take(limit)
            .toList()
    }

    private fun isItalian(meal: Meal): Boolean {
        for (tag in meal.tags) {
            for (keyword in ITALIAN_KEYWORDS) {
                if (tag.contains(keyword, ignoreCase = true)) {
                    return true
                }
            }
        }

        for (keyword in ITALIAN_KEYWORDS) {
            if (meal.name.contains(keyword, ignoreCase = true) ||
                meal.description.contains(keyword, ignoreCase = true)
            ) {
                return true
            }
        }

        return false
    }

    private fun isGoodForLargeGroups(meal: Meal): Boolean {
        if (meal.ingredientsCount < 5) return false
        if (meal.stepsCount < 3) return false

        for (keyword in LARGE_GROUP_KEYWORDS) {
            if (meal.description.contains(keyword, ignoreCase = true)) return true

            for (ingredient in meal.ingredients) {
                if (ingredient.contains(keyword, ignoreCase = true)) return true
            }
        }

        return meal.ingredientsCount >= 8
    }

    private companion object {
        val ITALIAN_KEYWORDS = listOf(
            "italian", "italy", "pasta", "pizza", "risotto",
            "lasagna", "carbonara", "tiramisu", "parmesan",
            "mozzarella", "prosciutto", "bruschetta", "focaccia",
            "spaghetti", "ravioli", "gnocchi", "penne", "fettuccine",
            "marinara", "alfredo", "bolognese", "cannelloni", "ziti"
        )
        val LARGE_GROUP_KEYWORDS = listOf(
            "pounds", "lbs", "kilos", "kg", "cups", "large", "party",
            "group", "crowd", "serving", "feeds", "family", "potluck",
            "gathering", "batch", "holiday", "celebration"
        )
    }

}
