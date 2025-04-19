package org.damascus.di

import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.useCase.GetFilteredMealsUseCase
import org.koin.dsl.module
import org.damascus.useCase.*

val useCaseModule = module {
    single { GetFirstTenMealsUseCase(get()) }
    single { IdentifyIraqiMealsUseCase(get()) }
    single { GetEasyFoodSuggestionsUseCase(get()) }
    single { SearchMealByNameUseCase(get()) }
    single { GetKetoMealUseCase(get()) }
    single { GetFilteredMealsUseCase(get() ) }
}