package ru.gozerov.core.di

/**
 *
 * Navigation through deeplinks
 *
 * */

enum class Screen(val route: String) {
    FaceCapturing("biometricHack://face_capturing"),
    PhotoCheck("biometricHack://photo_check"),
    VoiceRecord("biometricHack://voice_record"),
    StartPage("biometricHack://start_page"),
    FaceRulesPage("biometricHack://face_rules"),
    FaceRulesList("biometricHack://face_rules_list"),
    VoiceStart("biometricHack://take_voice_start"),
    RecordingList("biometricHack://recording_list"),
    FinalPage("biometricHack://final_page")
}