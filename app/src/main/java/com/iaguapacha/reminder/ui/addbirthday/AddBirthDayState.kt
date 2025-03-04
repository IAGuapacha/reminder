package com.iaguapacha.reminder.ui.addbirthday

import java.time.LocalDate

data class AddBirthdayState(
    val name: String = "",
    val day: String = "",
    val month: String = "",
    val year: String = "",
    val nameError: String? = null,
    val dayError: String? = null,
    val monthError: String? = null,
    val yearError: String? = null,
    val notifications: Set<NotificationType> = emptySet(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val navigationEvent: NavigationEvent? = null
)
