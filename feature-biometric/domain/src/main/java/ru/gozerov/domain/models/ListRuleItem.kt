package ru.gozerov.domain.models

data class ListRuleItem(
    val header: String,
    val desc: String,
    val descCorrect: String,
    val descIncorrect: String,
    val imageCorrect: String,
    val imageIncorrect: String
)