package org.damascus.di


import data.CsvFileReader
import data.CsvMealRepository
import org.damascus.data.CsvParser
import org.damascus.logic.MealRepository
import org.damascus.presentation.FoodChangeMoodUI
import org.damascus.utils.CSV_FILE_PATH
import org.koin.dsl.module

import java.io.File

val appModule = module {
    single { File(CSV_FILE_PATH) }
    single { CsvParser() }
    single { CsvFileReader() }

    single<MealRepository> { CsvMealRepository(get(), get()) }

    single { FoodChangeMoodUI(get()) }
}