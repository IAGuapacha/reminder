package com.iaguapacha.reminder.ui.birthdayform

data class BirthdayFormState(
    val birthdayId: Long? = null,
    val isEditMode: Boolean = false,
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
