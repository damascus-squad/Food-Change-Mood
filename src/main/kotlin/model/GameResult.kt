package org.damascus.model

data class GameResult(
    val score: Int,
    val correctAnswers: Int,
    val gameMessages: List<String>
)