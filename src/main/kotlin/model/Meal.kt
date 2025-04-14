package org.damascus.model

import model.Nutrition

data class Meal(
    val name: String,
    val id: String,
    val minutes: Int?,
    val contributorId: Int?,
    val submitted: String,
    val tags: List<String>,
    val nutrition: Nutrition,
    val nSteps: Int?,
    val steps: List<String>,
    val description: String,
    val ingredients: List<String>,
    val nIngredients: Int?
)
