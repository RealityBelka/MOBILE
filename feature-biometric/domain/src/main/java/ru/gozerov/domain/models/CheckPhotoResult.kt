package ru.gozerov.domain.models

data class CheckPhotoResult(
    val isPhotoValid: Boolean,
    val fail: String? = null
)
