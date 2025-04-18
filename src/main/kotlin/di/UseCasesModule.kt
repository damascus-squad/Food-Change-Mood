package org.damascus.di

import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.useCase.GetEasyFoodSuggestionsUseCase
import org.damascus.useCase.IdentifyIraqiMealsUseCase
import org.damascus.useCase.SearchMealByNameUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetFirstTenMealsUseCase(get()) }
    single { IdentifyIraqiMealsUseCase(get()) }
    single { GetEasyFoodSuggestionsUseCase(get()) }
    single { SearchMealByNameUseCase(get()) }
}