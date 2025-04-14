package org.damascus

import org.damascus.di.appModule
import org.damascus.di.useCaseModule
import org.damascus.presentation.FoodChangeMoodUI
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.getKoin

fun main() {
    println("Hello World!")

    startKoin{
        modules(appModule, useCaseModule)
    }

    val ui : FoodChangeMoodUI = getKoin().get()
    ui.start()
}