package org.damascus.logic

import org.damascus.model.Meal


interface MealRepository {
    fun getAllMeals(): List<Meal>
    fun getEggFreeSweet(): Meal
}