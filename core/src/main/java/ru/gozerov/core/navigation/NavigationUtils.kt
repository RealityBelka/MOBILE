package ru.gozerov.core.navigation

import android.net.Uri
import android.os.Bundle
import android.util.Log
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

fun NavController.launch(screen: Screen, vararg args: Any?) {
    var stringArgs = args.joinToString(separator = "/")
    if (stringArgs.isNotEmpty()) stringArgs = "/$stringArgs"

    navigate(Uri.parse("${screen.route}$stringArgs"))
}