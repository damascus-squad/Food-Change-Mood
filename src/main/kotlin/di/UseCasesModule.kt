package org.damascus.di

import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.useCase.*
import org.koin.dsl.module

val useCaseModule = module {
    single { GetFirstTenMealsUseCase(get()) }
    single { GetMealsByDateUseCase(get())}
    single { IdentifyIraqiMealsUseCase(get()) }
    single { GetEasyFoodSuggestionsUseCase(get()) }
    single { SearchMealByNameUseCase(get()) }
    single { GetKetoMealUseCase(get()) }
    single { GetMealGameUtilsUseCase(get()) }
    single { GetRandomMealUseCase(get() ) }
    single { GetHighCalorieMealUseCase(get()) }
}