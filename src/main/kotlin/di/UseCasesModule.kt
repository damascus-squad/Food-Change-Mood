package org.damascus.di

import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.useCase.GuessMealPreparationTimeUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetFirstTenMealsUseCase(get() ) }
    single { GuessMealPreparationTimeUseCase(get() ) }

}