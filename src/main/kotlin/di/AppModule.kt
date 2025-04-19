package org.damascus.di


import data.MealFileReader
import data.MealRepositoryImpl
import org.damascus.data.MealDataParser
import org.damascus.data.source.CsvMealDataSource
import org.damascus.logic.MealRepository
import org.damascus.presentation.FoodChangeMoodUI
import org.damascus.useCase.GetFilteredMealsUseCase
import org.damascus.utils.CSV_FILE_PATH
import org.koin.dsl.module

import java.io.File

val appModule = module {
    single { File(CSV_FILE_PATH) }
    single { MealDataParser() }
    single { MealFileReader() }

    single { CsvMealDataSource(get(), get()) }
    single<MealRepository> { MealRepositoryImpl(get()) }
    single { FoodChangeMoodUI(get(),get()) }
}