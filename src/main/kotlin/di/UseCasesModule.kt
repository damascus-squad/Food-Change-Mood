package org.damascus.di

import org.damascus.useCase.ExploreOtherCountriesFoodUseCase
import org.damascus.logic.GetFirstTenMealsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetFirstTenMealsUseCase(get() ) }
    single { ExploreOtherCountriesFoodUseCase(get() ) }
}