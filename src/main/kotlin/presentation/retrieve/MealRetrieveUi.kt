package org.damascus.presentation.fetch

import org.damascus.presentation.utils.ConsoleDisplay
import org.damascus.useCase.*

class MealRetrieveUi(
    private val consoleDisplay: ConsoleDisplay,
    private val identifyMealsByNationalityUseCase: IdentifyMealsByNationalityUseCase,
    private val potatoMealsUseCase: GetRandomPotatoMealsUseCase,
    private val healthyFastFoodMealsUseCase: GetHealthyFastFoodMealsUseCase,
    private val seafoodMealsByContentUseCase: SortSeafoodMealsByContentUseCase,
    private val italianLargeGroupMealsUseCase: GetItalianLargeGroupMealsUseCase
) : MealRetrieve {
    override fun getNationalityMeals() {
        consoleDisplay.mealsName(identifyMealsByNationalityUseCase())
    }

    override fun getPotatoMeals() {
        consoleDisplay.mealsName(potatoMealsUseCase())
    }

    override fun getHealthyMeal() {
        return consoleDisplay.mealsName(healthyFastFoodMealsUseCase())
    }

    override fun getSeafoodMeal() {
        consoleDisplay.mealsName(seafoodMealsByContentUseCase())
    }

    override fun getItalianMeals() {
        consoleDisplay.meals(italianLargeGroupMealsUseCase())
    }
}