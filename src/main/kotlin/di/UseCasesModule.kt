package org.damascus.di

import org.damascus.useCase.game.GetRandomMealUseCase
import org.damascus.useCase.retrieve.*
import org.damascus.useCase.search.ExploreOtherCountriesFoodUseCase
import org.damascus.useCase.search.FindMealsByCaloriesAndProtein
import org.damascus.useCase.search.GetMealsByDateUseCase
import org.damascus.useCase.search.SearchMealByNameUseCase
import org.damascus.useCase.suggest.GetEggFreeSweetUseCase
import org.damascus.useCase.suggest.GetHighCalorieMealUseCase
import org.damascus.useCase.suggest.GetKetoMealUseCase
import org.damascus.useCase.suggest.GetMealGameUtilsUseCase
import org.koin.dsl.module

val useCaseModule = module {
    single { ExploreOtherCountriesFoodUseCase(get()) }
    single { GetEasyFoodSuggestionsUseCase(get()) }
    single { GetEggFreeSweetUseCase(get()) }
    single { GetHealthyFastFoodMealsUseCase(get()) }
    single { GetHighCalorieMealUseCase(get()) }
    single { GetItalianLargeGroupMealsUseCase(get()) }
    single { GetKetoMealUseCase(get()) }
    single { GetMealGameUtilsUseCase(get()) }
    single { GetMealsByDateUseCase(get()) }
    single { GetRandomMealUseCase(get()) }
    single { GetRandomPotatoMealsUseCase(get()) }
    single { IdentifyMealsByNationalityUseCase(get()) }
    single { SearchMealByNameUseCase(get()) }
    single { SortSeafoodMealsByContentUseCase(get()) }
    single { FindMealsByCaloriesAndProtein(get()) }

}