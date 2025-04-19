package org.damascus.di

import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.useCase.GetMealsByDateUseCase

import org.damascus.useCase.GetEasyFoodSuggestionsUseCase
import org.damascus.useCase.GetEggFreeSweetUseCase
import org.damascus.useCase.GetHighCalorieMealUseCase
import org.damascus.useCase.IdentifyIraqiMealsUseCase
import org.damascus.useCase.SearchMealByNameUseCase
import org.damascus.useCase.GetKetoMealUseCase
import org.damascus.useCase.GetRandomMealUseCase
import org.koin.dsl.module


val useCaseModule = module {
    single { GetFirstTenMealsUseCase(get() ) }
    single { GetEasyFoodSuggestionsUseCase(get() ) }
    single { GetEggFreeSweetUseCase(get()) }
    single { IdentifyIraqiMealsUseCase(get()) }
    single { GetMealsByDateUseCase(get())}
    single { SearchMealByNameUseCase(get()) }
    single { GetKetoMealUseCase(get()) }
    single { GetHighCalorieMealUseCase(get()) }
    single { GetRandomMealUseCase(get()) }
}