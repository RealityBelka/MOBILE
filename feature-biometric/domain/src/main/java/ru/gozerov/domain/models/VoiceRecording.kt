package ru.gozerov.domain.models

data class VoiceRecording(
    val step: Int,
    val isSuccess: Boolean? = null,
    val fail: String? = null
)
