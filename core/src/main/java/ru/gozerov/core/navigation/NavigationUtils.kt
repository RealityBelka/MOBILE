package ru.gozerov.core.navigation

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.NavController

/**
 *
 * Get extension method for navigator
 *
 * To use that you need implement interface Navigator in activity
 *
 * */

val Fragment.navigator get() = (requireActivity() as Navigator)

/**
 *
 * Extension method for navigation through deeplinks
 *
 * */

fun NavController.launch(screen: Screen) {
    navigate(Uri.parse(screen.route))
}