package org.damascus.di

import org.damascus.useCase.GetEggFreeSweetUseCase
import org.damascus.useCase.GetFirstTenMealsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetFirstTenMealsUseCase(get()) }
    single {
        GetEggFreeSweetUseCase(get())
    }
}