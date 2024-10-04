package ru.gozerov.core.navigation

/**
 *
 * Navigation through deeplinks
 *
 * */

enum class Screen(val route: String) {
    Biometric("biometricHack://biometric"),
    VoiceRecord("biometricHack://voice_record")
}