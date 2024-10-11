package ru.gozerov.core.navigation

/**
 *
 * Navigation through deeplinks
 *
 * */

enum class Screen(val route: String) {
    Biometric("biometricHack://biometric"),
    VoiceRecord("biometricHack://voice_record"),
    StartPage("biometricHack://start_page"),
    FaceRulesPage("biometricHack://face_rules"),
    FaceRulesList("biometricHack://face_rules_list"),
    VoiceStart("biometricHack://take_voice_start"),
    RecordingList("biometricHack://recording_list")
}