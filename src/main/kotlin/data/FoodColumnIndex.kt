package org.damascus.data

object FoodColumnIndex {
    const val NAME = 0
    const val ID = 1
    const val MINUTES = 2
    const val CONTRIBUTOR_ID = 3
    const val SUBMITTED = 4
    const val TAGS = 5
    const val NUTRITION = 6
    const val N_STEPS = 7
    const val STEPS = 8
    const val DESCRIPTION = 9
    const val INGREDIENTS = 10
    const val N_INGREDIENTS = 11

    fun getOrderedValues(map: Map<String, String>): List<String> {
        return listOf(
            map["name"] ?: "",
            map["id"] ?: "0",
            map["minutes"] ?: "0",
            map["contributor_id"] ?: "0",
            map["submitted"] ?: "",
            map["tags"] ?: "[]",
            map["nutrition"] ?: "[]",
            map["n_steps"] ?: "0",
            map["steps"] ?: "[]",
            map["description"] ?: "",
            map["ingredients"] ?: "[]",
            map["n_ingredients"] ?: "0"
        )
    }
}