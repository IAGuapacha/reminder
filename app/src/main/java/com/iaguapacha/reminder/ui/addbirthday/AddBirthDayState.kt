package com.iaguapacha.reminder.ui.addbirthday

import java.time.LocalDate

data class AddBirthdayState(
    val name: String = "",
    val date: LocalDate = LocalDate.now(),
    val notifications: Set<NotificationType> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val navigationEvent: NavigationEvent? = null
)
