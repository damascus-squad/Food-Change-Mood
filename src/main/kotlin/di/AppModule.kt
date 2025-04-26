package org.damascus.di


import data.MealFileReader
import data.MealRepositoryImpl
import org.damascus.data.MealDataParser
import org.damascus.data.source.CsvMealDataSource
import org.damascus.logic.GuessIngredientGame
import org.damascus.logic.MealRepository
import org.damascus.presentation.FoodChangeMoodUi
import org.damascus.presentation.fetch.MealRetrieveUi
import org.damascus.presentation.game.GuessGameUI
import org.damascus.presentation.game.IngredientGameUi
import org.damascus.presentation.search.MealSearchUi
import org.damascus.presentation.suggest.MealSuggestUi
import org.damascus.presentation.utils.ConsoleDisplay
import org.damascus.presentation.utils.ConsoleUserInput
import org.damascus.utils.CSV_FILE_PATH
import org.koin.dsl.module
import java.io.File

val appModule = module {
    single { File(CSV_FILE_PATH) }
    single { MealDataParser() }
    single { MealFileReader() }

    single { CsvMealDataSource(get(), get()) }
    single<MealRepository> { MealRepositoryImpl(get()) }


    single { IngredientGameUi(get()) }
    single { GuessIngredientGame(get()) }
    single { ConsoleUserInput() }
    single { ConsoleDisplay() }
    single { GuessGameUI(get()) }
    single { MealSearchUi(get(), get(), get(), get(), get(), get()) }
    single { MealSuggestUi(get(), get(), get(), get(), get()) }
    single { MealRetrieveUi(get(), get(), get(), get(), get(), get()) }

    single {
        FoodChangeMoodUi(
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }
}
