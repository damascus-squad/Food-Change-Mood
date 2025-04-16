package org.damascus.di

import org.damascus.useCase.GetEggFreeSweetUseCase
import org.damascus.useCase.GetFirstTenMealsUseCase
import org.damascus.useCase.GetHighCalorieMealUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { GetFirstTenMealsUseCase(get()) }
    single {
        GetEggFreeSweetUseCase(get())
    }

    single { GetEggFreeSweetUseCase(get()) }
    single { GetHighCalorieMealUseCase(get()) }

}