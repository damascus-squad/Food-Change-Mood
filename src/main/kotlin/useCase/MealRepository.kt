package org.damascus.useCase

import org.damascus.model.Meal


interface MealRepository {
    fun getAllMeals(): List<Meal>
    fun getEggFreeSweet(): Meal

}