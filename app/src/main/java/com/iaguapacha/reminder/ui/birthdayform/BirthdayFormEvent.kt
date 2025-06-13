package com.iaguapacha.reminder.ui.birthdayform

import java.time.LocalDate

sealed interface BirthdayFormEvent {

    data class NameChanged(val name: String): BirthdayFormEvent
    data class DayChanged(val day: String) : BirthdayFormEvent
    data class MonthChanged(val month: String) : BirthdayFormEvent
    data class YearChanged(val year: String) : BirthdayFormEvent
    data class NotificationToggled(val type: NotificationType): BirthdayFormEvent
    object Save : BirthdayFormEvent
    object ErrorShown : BirthdayFormEvent
    object Navigated : BirthdayFormEvent
}
