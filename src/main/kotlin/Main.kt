package org.damascus

import org.damascus.di.appModule
import org.damascus.di.useCaseModule
import org.damascus.presentation.FoodChangeMoodUi
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

fun main() {
    startKoin {
        modules(appModule, useCaseModule)
    }

    val ui: FoodChangeMoodUi = getKoin().get()
    ui.start()
}