package com.iaguapacha.reminder.navigation

sealed class NavRoutes(val route: String) {
    object Main : NavRoutes("main")
    object AddBirthday : NavRoutes("addBirthday")
    object BirthdayDetail : NavRoutes("birthdayDetail/{reminderId}") {
        fun createRoute(reminderId: Long) = "birthdayDetail/$reminderId"
    }
    object Birthdays : NavRoutes("birthdays")
}

