package org.damascus.di

import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.useCase.*

data class MealUseCases(
    val getFirstNMeals: GetFirstTenMealsUseCase,
    val getEasyFoodSuggestions: GetEasyFoodSuggestionsUseCase,
    val getEggFreeSweet: GetEggFreeSweetUseCase,
    val getHealthyFastFoodMeals: GetHealthyFastFoodMealsUseCase,
    val getHighCalorieMeal: GetHighCalorieMealUseCase,
    val getItalianLargeGroupMeals: GetItalianLargeGroupMealsUseCase,
    val getKetoMeal: GetKetoMealUseCase,
    val getMealGameUtils: GetMealGameUtilsUseCase,
    val getMealsByDate: GetMealsByDateUseCase,
    val getRandomMeal: GetRandomMealUseCase,
    val getRandomPotatoMeals: GetRandomPotatoMealsUseCase,
    val identifyIraqiMeals: IdentifyIraqiMealsUseCase,
    val searchMealByName: SearchMealByNameUseCase,
    val sortSeafoodMeals: SortSeafoodMealsByContentUseCase,
    val findMealsByCaloriesAndProtein: FindMealsByCaloriesAndProtein,
    val exploreOtherCountriesFood: ExploreOtherCountriesFoodUseCase
)
