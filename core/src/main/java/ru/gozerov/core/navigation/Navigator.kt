package ru.gozerov.core.navigation

/**
 *
 * Use that method to navigate through actions by navigation controller
 *
 * Also you need implement Navigator interface in activity to handle actions
 *
 * */
interface Navigator {

    fun launch(screen: Screen)

}