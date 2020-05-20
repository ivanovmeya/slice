package com.ivanovme.slice.presentation.game

data class GameViewState(
    val title: String,
    val task: String,
    val questions: List<Question>,
    val firstOption: String,
    val secondOption: String
)