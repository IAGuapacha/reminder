package com.iaguapacha.reminder.ui.addbirthday

import java.time.LocalDate

sealed interface AddBirthdayEvent {

    data class NameChanged(val name: String): AddBirthdayEvent
    data class DayChanged(val day: String) : AddBirthdayEvent
    data class MonthChanged(val month: String) : AddBirthdayEvent
    data class YearChanged(val year: String) : AddBirthdayEvent
    data class NotificationToggled(val type: NotificationType): AddBirthdayEvent
    object Save : AddBirthdayEvent
    object ErrorShown : AddBirthdayEvent
    object Navigated : AddBirthdayEvent
}
