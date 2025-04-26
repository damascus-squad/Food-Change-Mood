package org.damascus.presentation.io

import org.damascus.model.Meal

interface Display {
    fun displayMealsBy(
        meals: List<Meal>,
        label: String,
        contentSelector: ((Meal) -> Map<String, Any?>)? = null
    )
}