package org.damascus.presentation.search

import org.damascus.model.Meal

interface MealSearch {
    fun getByName()
    fun getByCountry()
    fun getByDate()
    fun getByCaloriesAndProtein()
}