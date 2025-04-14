package org.damascus.logic

import org.damascus.model.Meal


interface FoodRepo {

    fun getAllMeals(): List<Meal>
}