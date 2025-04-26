package org.damascus.useCase.retrieve

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class GetItalianLargeGroupMealsUseCase(
    private val repo: MealRepository
) {

    private val italianKeywords = listOf("italian", "italy")

    operator fun invoke(): List<Meal> {
        return repo.getAllMeals().filter {
            isItalian(it) && isGoodForLargeGroups(it) && isOriginal(it)
        }
    }

    private fun isItalian(meal: Meal): Boolean {
        italianKeywords.forEach { keyword ->
            if (meal.name.contains(keyword, ignoreCase = true)
                || meal.description.contains(keyword, ignoreCase = true)
                || meal.tags.any { it.contains(keyword, ignoreCase = true) }
            ) return true
        }

        return false
    }

    private fun isGoodForLargeGroups(meal: Meal): Boolean {
        return meal.tags.any { it.contains(LARGE_GROUP_TAG, ignoreCase = true) }
    }

    private fun isOriginal(meal: Meal): Boolean {
        return meal.description.contains(ORIGINAL, ignoreCase = true)
                || meal.tags.any { it.contains(ORIGINAL, ignoreCase = true)
        }
    }

    companion object {
        const val LARGE_GROUP_TAG = "for-large-groups"
        const val ORIGINAL = "original"
    }

}
