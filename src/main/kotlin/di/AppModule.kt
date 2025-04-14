package org.damascus.di


import data.CsvFileReader
import data.CsvMealRepository
import org.damascus.data.CsvParser
import org.damascus.logic.MealRepository
import org.damascus.presentation.FoodChangeMoodUI
import org.koin.dsl.module

import java.io.File

val appModule = module {
    single { File("assets/food.csv") }
    single { CsvParser() }
    single { CsvFileReader() }

    single<MealRepository> { CsvMealRepository(get(), get()) }

    single { FoodChangeMoodUI(get()) }
}