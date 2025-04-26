package org.damascus.presentation.retrieve

import org.damascus.presentation.io.ConsoleDisplay
import org.damascus.useCase.retrieve.*

class MealRetrieveUi(
    private val consoleDisplay: ConsoleDisplay,
    private val identifyMealsByNationalityUseCase: IdentifyMealsByNationalityUseCase,
    private val potatoMealsUseCase: GetRandomPotatoMealsUseCase,
    private val healthyFastFoodMealsUseCase: GetHealthyFastFoodMealsUseCase,
    private val seafoodMealsByContentUseCase: SortSeafoodMealsByContentUseCase,
    private val italianLargeGroupMealsUseCase: GetItalianLargeGroupMealsUseCase,
    private val easyFoodSuggestionsUseCase: GetEasyFoodSuggestionsUseCase,
) : MealRetrieve {

    override fun getNationalityMeals() {
        consoleDisplay.displayMealsBy(
            meals = identifyMealsByNationalityUseCase(),
            label = "Nationality"
        ) { meal -> mapOf("Name" to meal.name) }
    }

    override fun getPotatoMeals() {
        consoleDisplay.displayMealsBy(
            meals = potatoMealsUseCase(),
            label = "Potato Meals"
        ) { meal -> mapOf("Name" to meal.name) }
    }

    override fun getHealthyMeal() {
        return consoleDisplay.displayMealsBy(
            meals = healthyFastFoodMealsUseCase(),
            label = "Healthy Meals"
        )
    }

    override fun getSeafoodMeal() {
        consoleDisplay.displayMealsBy(
            meals = seafoodMealsByContentUseCase(),
            label = "Seafood"
        ) { meal -> mapOf("Name" to meal.name) }
    }

    override fun getItalianMeals() {
        consoleDisplay.displayMealsBy(
            meals = italianLargeGroupMealsUseCase(),
            label = "Italian Meals"
        ) { meal -> mapOf("Name" to meal.name) }
    }

    override fun getEasyFood() {
        consoleDisplay.displayMealsBy(
            meals = easyFoodSuggestionsUseCase(),
            label = "Italian Meals"
        ) { meal ->
            mapOf(
                "Name" to meal.name,
                "Preparation Time" to meal.minutes,
                "Ingredients Count" to meal.ingredientsCount,
                "Ingredients" to meal.ingredients,
                "Steps Count" to meal.stepsCount,
                "Steps" to meal.steps
            )
        }
    }
}