package org.damascus.di


import data.MealFileReader
import data.MealRepositoryImpl
import org.damascus.data.MealDataParser
import org.damascus.data.source.CsvMealDataSource
import org.damascus.logic.GetFirstTenMealsUseCase
import org.damascus.logic.MealRepository
import org.damascus.presentation.FoodChangeMoodUi
import org.damascus.useCase.*
import org.damascus.utils.CSV_FILE_PATH
import org.koin.dsl.module
import java.io.File

val appModule = module {
    single { File(CSV_FILE_PATH) }
    single { MealDataParser() }
    single { MealFileReader() }

    single { CsvMealDataSource(get(), get()) }
    single<MealRepository> { MealRepositoryImpl(get()) }

    single { GetFirstTenMealsUseCase(get()) }
    single { GetEasyFoodSuggestionsUseCase(get()) }
    single { GetKetoMealUseCase(get()) }
    single { IdentifyMealsByNationalityUseCase(get()) }
    single { SearchMealByNameUseCase(get()) }
    single { GetMealsByDateUseCase(get()) }

    single {
        FoodChangeMoodUi(
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get(),
            get()
        )
    }
}
