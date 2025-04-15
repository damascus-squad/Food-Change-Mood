package org.damascus.model

import model.Nutrition

data class Meal(
    val name: String,
    val id: Int,
    val minutes: Int,
    val contributorId: Int,
    val submitted: String,
    val tags: List<String>,
    val nutrition: Nutrition,
    val stepsCount: Int,
    val steps: List<String>,
    val description: String,
    val ingredients: List<String>,
    val ingredientsCount: Int
)
