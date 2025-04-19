package org.damascus.useCase

import org.damascus.model.Meal

class GetWrongIngredientUseCase {
    operator fun invoke(
        validMeal: List<Meal>,
        currentRandomMeal: Meal,
        correctIngredient: String
    ): List<String> {
        val wrongIngredients = validMeal
            .filter { validMeal -> validMeal == currentRandomMeal }
            .flatMap { validMeal -> validMeal.ingredients }
            .filter { ingredient -> ingredient != correctIngredient }
            .distinct()
            .take(2)
        return wrongIngredients
    }

}