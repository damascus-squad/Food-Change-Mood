package org.damascus.presentation.search

interface MealSearch {
    fun getByName()
    fun getByCountry()
    fun getByDate()
    fun getByCaloriesAndProtein()
}