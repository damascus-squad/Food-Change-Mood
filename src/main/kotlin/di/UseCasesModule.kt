package org.damascus.di

import org.damascus.useCase.*
import org.koin.dsl.module

val useCaseModule = module {
    single { ExploreOtherCountriesFoodUseCase(get()) }
    single { GetEasyFoodSuggestionsUseCase(get()) }
    single { GetEggFreeSweetUseCase(get()) }
    single { GetHealthyFastFoodMealsUseCase(get()) }
    single { GetHighCalorieMealUseCase(get()) }
    single { GetItalianLargeGroupMealsUseCase(get()) }
    single { GetKetoMealUseCase(get()) }
    single { GetMealGameUtilsUseCase(get()) }
    single { GetMealsByDateUseCase(get()) }
    single { GetRandomMealUseCase(get()) }
    single { GetRandomPotatoMealsUseCase(get()) }
    single { IdentifyMealsByNationalityUseCase(get()) }
    single { SearchMealByNameUseCase(get()) }
    single { SortSeafoodMealsByContentUseCase(get()) }
    single { FindMealsByCaloriesAndProtein(get()) }

}