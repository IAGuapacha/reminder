package com.iaguapacha.reminder.ui.birthdayform


sealed interface BirthdayFormEvent {
    data class NameChanged(val name: String): BirthdayFormEvent
    data class DateChanged(val day: String, val month: String, val year: String): BirthdayFormEvent
    data class NotificationToggled(val type: NotificationType): BirthdayFormEvent
    object Save : BirthdayFormEvent
    object ErrorShown : BirthdayFormEvent
    object Navigated : BirthdayFormEvent
}
