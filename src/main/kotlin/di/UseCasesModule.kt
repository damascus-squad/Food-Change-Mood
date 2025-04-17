package org.damascus.di

import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.logic.GetKetoMealUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetFirstTenMealsUseCase(get() ) }
    single { GetKetoMealUseCase(get()) }
}