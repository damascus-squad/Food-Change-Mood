package org.damascus.di

import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.useCase.GetRandomPotatoMealsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetFirstTenMealsUseCase(get() ) }
    single { GetRandomPotatoMealsUseCase(get()) }

}