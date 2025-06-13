package com.iaguapacha.reminder.navigation

sealed class NavRoutes(val route: String) {
    object Main : NavRoutes("main")
    object AddBirthday : NavRoutes("addBirthday") // Mantenemos para compatibilidad
    object BirthdayForm : NavRoutes("birthdayForm?birthdayId={birthdayId}") {
        fun createRoute(birthdayId: Long? = null) =
            if (birthdayId != null) "birthdayForm?birthdayId=$birthdayId" else "birthdayForm"
    }
    object BirthdayDetail : NavRoutes("birthdayDetail/{reminderId}") {
        fun createRoute(reminderId: Long) = "birthdayDetail/$reminderId"
    }
}
