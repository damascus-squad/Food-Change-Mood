package org.damascus.di

import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.useCase.GetEasyFoodSuggestionsUseCase
import org.damascus.useCase.GetEggFreeSweetUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetFirstTenMealsUseCase(get() ) }
    single { GetEasyFoodSuggestionsUseCase(get() ) }
    single { GetEggFreeSweetUseCase(get()) }
}