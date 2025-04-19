package org.damascus.useCase

import org.damascus.logic.MealRepository
import org.damascus.model.Meal

class GetKetoMealUseCase(
    private val repo: MealRepository
) {

    operator fun invoke(): List<Meal> {
        val ketoMeals = repo.getAllMeals().filter {
            isKetoFriendly(it)
        }
        return ketoMeals
    }

    private fun isKetoFriendly(meal: Meal): Boolean {
        val nutrition = meal.nutrition

        if (nutrition.calories <= 0
            || nutrition.carbohydrates > MAX_CARBS_GRAMS
            || nutrition.totalFat < 0
            || nutrition.protein < 0
        )
            return false

        val fatPercentage = (nutrition.totalFat * CALORIES_PER_GRAM_FAT / nutrition.calories) * 100
        val proteinPercentage = (nutrition.protein * CALORIES_PER_GRAM_PROTEIN / nutrition.calories) * 100

        return fatPercentage >= MIN_FAT_PERCENTAGE
                && proteinPercentage in MIN_PROTEIN_PERCENTAGE..MAX_PROTEIN_PERCENTAGE
    }

    private companion object {
        private const val CALORIES_PER_GRAM_FAT = 9.0
        private const val CALORIES_PER_GRAM_PROTEIN = 4.0
        private const val MAX_CARBS_GRAMS = 15.0
        private const val MIN_FAT_PERCENTAGE = 70.0
        private const val MIN_PROTEIN_PERCENTAGE = 10.0
        private const val MAX_PROTEIN_PERCENTAGE = 20.0
    }
}